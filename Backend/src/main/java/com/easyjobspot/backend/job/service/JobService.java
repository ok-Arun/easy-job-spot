package com.easyjobspot.backend.job.service;

import com.easyjobspot.backend.audit.enums.AuditAction;
import com.easyjobspot.backend.audit.service.AuditLogService;
import com.easyjobspot.backend.exception.BadRequestException;
import com.easyjobspot.backend.exception.ResourceNotFoundException;
import com.easyjobspot.backend.job.dto.JobDTO;
import com.easyjobspot.backend.job.dto.request.JobCreateRequest;
import com.easyjobspot.backend.job.dto.request.JobUpdateRequest;
import com.easyjobspot.backend.job.entity.Job;
import com.easyjobspot.backend.job.mapper.JobMapper;
import com.easyjobspot.backend.job.repository.JobRepository;
import com.easyjobspot.backend.profile.entity.ProviderProfile;
import com.easyjobspot.backend.profile.repository.ProviderProfileRepository;
import com.easyjobspot.backend.security.UserPrincipal;
import com.easyjobspot.backend.user.entity.User;
import com.easyjobspot.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final UserRepository userRepository;
    private final ProviderProfileRepository providerProfileRepository;
    private final AuditLogService auditLogService;

    // ====================================================
    // PUBLIC JOB LISTING
    // ====================================================
    @Transactional(readOnly = true)
    public Page<JobDTO> getAllJobs(
            int page,
            int size,
            String sortBy,
            String search,
            String category,
            String title,      // ⭐ NEW
            String location    // ⭐ NEW
    ) {

        Pageable pageable =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));

        Page<Job> jobs;

        // 1️⃣ NEW — TITLE / LOCATION FILTER (highest priority)
        if ((title != null && !title.trim().isEmpty())
                || (location != null && !location.trim().isEmpty())) {

            jobs = jobRepository.filterByTitleAndLocation(
                    Job.JobStatus.ACTIVE,
                    (title != null && !title.isBlank()) ? title.trim() : null,
                    (location != null && !location.isBlank()) ? location.trim() : null,
                    pageable
            );
        }

        // 2️⃣ EXISTING — KEYWORD SEARCH
        else if (search != null && !search.trim().isEmpty()) {
            jobs = jobRepository.searchByStatus(
                    Job.JobStatus.ACTIVE,
                    search.trim(),
                    pageable
            );
        }

        // 3️⃣ EXISTING — CATEGORY FILTER
        else if (category != null && !category.trim().isEmpty()) {
            jobs = jobRepository.findByStatusAndCategory(
                    Job.JobStatus.ACTIVE,
                    category.trim(),
                    pageable
            );
        }

        // 4️⃣ EXISTING — ALL ACTIVE JOBS
        else {
            jobs = jobRepository.findByStatus(Job.JobStatus.ACTIVE, pageable);
        }

        return jobs.map(jobMapper::toDTO);
    }

    // ====================================================
    // GET JOB BY ID (PUBLIC)
    // ====================================================
    @Transactional(readOnly = true)
    public JobDTO getJobById(UUID id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (job.getStatus() != Job.JobStatus.ACTIVE) {
            throw new ResourceNotFoundException("Job not available");
        }

        return jobMapper.toDTO(job);
    }

    // ====================================================
    // CREATE JOB (PROVIDER / ADMIN)
    // ====================================================
    @Transactional
    public JobDTO createJob(JobCreateRequest request) {

        User user = getCurrentUser();

        if (user.getUserType() != User.UserType.JOB_PROVIDER
                && !user.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Only job providers can post jobs");
        }

        if (user.getUserType() == User.UserType.JOB_PROVIDER) {
            ProviderProfile profile =
                    providerProfileRepository.findByUserId(user.getId()).orElse(null);

            if (profile == null || !profile.isProfileCompleted()) {
                throw new BadRequestException("COMPLETE_PROVIDER_PROFILE_BEFORE_POSTING_JOB");
            }
        }

        Job.JobType jobType;
        try {
            jobType = Job.JobType.valueOf(request.getJobType().toUpperCase());
        } catch (Exception e) {
            throw new BadRequestException("Invalid job type");
        }

        boolean exists =
                jobRepository.existsByTitleIgnoreCaseAndCompanyIgnoreCaseAndLocationIgnoreCaseAndJobTypeAndCreatedBy(
                        request.getTitle().trim(),
                        request.getCompany().trim(),
                        request.getLocation().trim(),
                        jobType,
                        user.getId()
                );

        if (exists) {
            throw new BadRequestException("This job is already posted by you.");
        }

        Job job = Job.builder()
                .title(request.getTitle().trim())
                .company(request.getCompany().trim())
                .category(request.getCategory().trim())
                .location(request.getLocation().trim())
                .jobType(jobType)
                .description(request.getDescription())
                .workMode(request.getWorkMode())
                .employmentLevel(request.getEmploymentLevel())
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .experienceMin(request.getExperienceMin())
                .experienceMax(request.getExperienceMax())
                .vacancyCount(request.getVacancyCount())
                .applicationType(request.getApplicationType())
                .applicationUrl(request.getApplicationUrl())
                .deadline(request.getDeadline())
                .createdBy(user.getId())
                .build();

        return jobMapper.toDTO(jobRepository.save(job));
    }

    // ====================================================
    // UPDATE JOB (ADMIN OR OWNER)
    // ====================================================
    @Transactional
    public JobDTO updateJob(UUID id, JobUpdateRequest request) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        User user = getCurrentUser();

        if (!user.getRole().name().equals("ADMIN")
                && !job.getCreatedBy().equals(user.getId())) {
            throw new AccessDeniedException("Not allowed");
        }

        job.update(
                request.getTitle(),
                request.getCompany(),
                request.getCategory(),
                request.getLocation(),
                Job.JobType.valueOf(request.getJobType().toUpperCase()),
                request.getDescription()
        );

        return jobMapper.toDTO(jobRepository.save(job));
    }

    // ====================================================
    // ADMIN — REMOVE JOB (SOFT REMOVE)
    // ====================================================
    @Transactional
    public void removeJobByAdmin(UUID jobId) {

        User admin = getCurrentUser();

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Only admin allowed");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        job.removeByAdmin();
        jobRepository.save(job);

        auditLogService.log(
                admin.getId(),
                AuditAction.JOB_REMOVED_BY_ADMIN,
                job.getId(),
                "Job removed by admin"
        );
    }


    // ====================================================
    // ADMIN — RESTORE JOB
    // ====================================================
    @Transactional
    public void restoreJobByAdmin(UUID jobId) {

        User admin = getCurrentUser();

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Only admin allowed");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        job.restoreByAdmin();
        jobRepository.save(job);

        auditLogService.log(
                admin.getId(),
                AuditAction.JOB_RESTORED_BY_ADMIN,
                job.getId(),
                "Job restored by admin"
        );
    }



    // ====================================================
    // CLOSE JOB (ADMIN OR PROVIDER OWNER)
    // ====================================================
    @Transactional
    public void closeJob(UUID jobId) {

        User user = getCurrentUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        boolean isAdmin = user.getRole().name().equals("ADMIN");
        boolean isOwner = job.getCreatedBy().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Not allowed to close this job");
        }

        job.close();
        jobRepository.save(job);

        auditLogService.log(
                user.getId(),
                AuditAction.JOB_CLOSED,
                job.getId(),
                "Job closed"
        );
    }

    // ====================================================
// ADMIN — GET JOB BY ID (ANY STATUS)
// ====================================================
    @Transactional(readOnly = true)
    public JobDTO getJobByIdForAdmin(UUID id) {

        User admin = getCurrentUser();

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Only admin allowed");
        }

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        return jobMapper.toDTO(job);
    }

    // ====================================================
    // REOPEN JOB (ADMIN OR PROVIDER OWNER)
    // ====================================================
    @Transactional
    public void reopenJob(UUID jobId) {

        User user = getCurrentUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        boolean isAdmin = user.getRole().name().equals("ADMIN");
        boolean isOwner = job.getCreatedBy().equals(user.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("Not allowed to reopen this job");
        }

        if (job.getStatus() != Job.JobStatus.CLOSED) {
            throw new BadRequestException("Only CLOSED jobs can be reopened");
        }

        job.activate();
        jobRepository.save(job);

        auditLogService.log(
                user.getId(),
                AuditAction.JOB_APPROVED,
                job.getId(),
                "Job reopened (activated)"
        );
    }

    // ====================================================
    // PROVIDER — GET OWN JOBS (WITH APPLICATION COUNT)
    // ====================================================
    @Transactional(readOnly = true)
    public Page<com.easyjobspot.backend.job.dto.response.ProviderJobListItemDTO> getJobsByCurrentProvider(
            int page,
            int size,
            String status
    ) {

        User user = getCurrentUser();

        if (user.getUserType() != User.UserType.JOB_PROVIDER
                && !user.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Only providers can access their jobs");
        }

        Job.JobStatus jobStatus = null;

        if (status != null && !status.isBlank()) {
            try {
                jobStatus = Job.JobStatus.valueOf(status.toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("Invalid job status");
            }
        }

        // ⭐ fetch jobs with application count
        List<Object[]> rows =
                jobRepository.fetchProviderJobsWithApplicationCount(
                        user.getId(),
                        jobStatus
                );

        // ⭐ map to DTO
        List<com.easyjobspot.backend.job.dto.response.ProviderJobListItemDTO> list =
                rows.stream()
                        .map(r -> new com.easyjobspot.backend.job.dto.response.ProviderJobListItemDTO(
                                (UUID) r[0],
                                (String) r[1],
                                (String) r[2],
                                (String) r[3],
                                (String) r[4],
                                (Job.JobType) r[5],
                                (Job.JobStatus) r[6],
                                (LocalDateTime) r[7],
                                (Long) r[8]
                        ))
                        .toList();

        // ⭐ manual pagination (since query returns List)
        int start = Math.min(page * size, list.size());
        int end = Math.min(start + size, list.size());

        List<com.easyjobspot.backend.job.dto.response.ProviderJobListItemDTO> pageContent =
                list.subList(start, end);

        return new PageImpl<>(
                pageContent,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")),
                list.size()
        );
    }



    // ====================================================
    // ADMIN — VIEW ALL JOBS (OPTIONAL STATUS FILTER)
    // ====================================================
    @Transactional(readOnly = true)
    public Page<JobDTO> getAllJobsForAdmin(String status, int page, int size) {

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Job> jobs;

        if (status == null || status.isBlank()) {
            jobs = jobRepository.findAll(pageable);
        } else {

            Job.JobStatus jobStatus;

            try {
                jobStatus = Job.JobStatus.valueOf(status.toUpperCase());
            } catch (Exception e) {
                throw new BadRequestException("Invalid job status");
            }

            jobs = jobRepository.findByStatus(jobStatus, pageable);
        }

        return jobs.map(jobMapper::toDTO);
    }



    // ====================================================
    // ADMIN — VIEW PENDING JOBS
    // ====================================================
    @Transactional(readOnly = true)
    public Page<JobDTO> getPendingJobs(int page, int size) {

        User admin = getCurrentUser();

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Only admin allowed");
        }

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("createdAt").descending());

        return jobRepository
                .findByStatusOrderByCreatedAtDesc(Job.JobStatus.PENDING_APPROVAL, pageable)
                .map(jobMapper::toDTO);
    }

    // ====================================================
    // ADMIN — APPROVE JOB
    // ====================================================
    @Transactional
    public void approveJob(UUID jobId) {

        User admin = getCurrentUser();

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Only admin allowed");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        job.approve();
        jobRepository.save(job);

        auditLogService.log(
                admin.getId(),
                AuditAction.JOB_APPROVED,
                job.getId(),
                "Job approved"
        );
    }

    // ====================================================
    // ADMIN — REJECT JOB
    // ====================================================
    @Transactional
    public void rejectJob(UUID jobId, String reason) {

        if (reason == null || reason.trim().isEmpty()) {
            throw new BadRequestException("Rejection reason is required");
        }

        User admin = getCurrentUser();

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Only admin allowed");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        job.reject(reason.trim());
        jobRepository.save(job);

        auditLogService.log(
                admin.getId(),
                AuditAction.JOB_REJECTED,
                job.getId(),
                reason.trim()
        );
    }

    // ====================================================
    // SCHEDULER — AUTO EXPIRE JOBS
    // ====================================================
    @Transactional
    public void expireJobs() {

        List<Job> jobs =
                jobRepository.findByStatusAndDeadlineBefore(
                        Job.JobStatus.ACTIVE,
                        LocalDateTime.now()
                );

        jobs.forEach(Job::expire);
        jobRepository.saveAll(jobs);
    }

    // ====================================================
    // CURRENT USER
    // ====================================================
    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
