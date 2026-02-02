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
    public Page<JobDTO> getAllJobs(int page, int size, String sortBy, String search, String category) {

        Pageable pageable =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));

        Page<Job> jobs;

        if (search != null && !search.trim().isEmpty()) {
            jobs = jobRepository.searchByStatus(Job.JobStatus.ACTIVE, search.trim(), pageable);
        } else if (category != null && !category.trim().isEmpty()) {
            jobs = jobRepository.findByStatusAndCategory(Job.JobStatus.ACTIVE, category.trim(), pageable);
        } else {
            jobs = jobRepository.findByStatus(Job.JobStatus.ACTIVE, pageable);
        }

        return jobs.map(jobMapper::toDTO);
    }

    // ====================================================
    // GET JOB BY ID
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
    // CREATE JOB
    // ====================================================
    @Transactional
    public JobDTO createJob(JobCreateRequest request) {

        User user = getCurrentUser();

        if (!user.getRole().name().equals("ADMIN")
                && user.getUserType() != User.UserType.JOB_PROVIDER) {
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
                .deadline(request.getDeadline())
                .createdBy(user.getId())
                .build();

        return jobMapper.toDTO(jobRepository.save(job));
    }

    // ====================================================
    // UPDATE JOB
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
    // DELETE JOB
    // ====================================================
    @Transactional
    public void deleteJob(UUID id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        User user = getCurrentUser();

        if (!user.getRole().name().equals("ADMIN")
                && !job.getCreatedBy().equals(user.getId())) {
            throw new AccessDeniedException("Not allowed");
        }

        jobRepository.delete(job);
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
    // ADMIN — CLOSE JOB
    // ====================================================
    @Transactional
    public void closeJob(UUID jobId) {

        User admin = getCurrentUser();

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Only admin allowed");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        job.close();
        jobRepository.save(job);

        auditLogService.log(
                admin.getId(),
                AuditAction.JOB_CLOSED,
                job.getId(),
                "Job closed"
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
