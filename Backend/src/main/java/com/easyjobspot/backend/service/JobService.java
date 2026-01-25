package com.easyjobspot.backend.service;

import com.easyjobspot.backend.dto.request.JobCreateRequest;
import com.easyjobspot.backend.dto.request.JobUpdateRequest;
import com.easyjobspot.backend.dto.response.JobDTO;
import com.easyjobspot.backend.entity.Job;
import com.easyjobspot.backend.entity.Job.JobStatus;
import com.easyjobspot.backend.entity.User;
import com.easyjobspot.backend.exception.BadRequestException;
import com.easyjobspot.backend.exception.ResourceNotFoundException;
import com.easyjobspot.backend.mapper.JobMapper;
import com.easyjobspot.backend.repository.JobRepository;
import com.easyjobspot.backend.repository.UserRepository;
import com.easyjobspot.backend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;
    private final UserRepository userRepository;

    // ====================================================
    // PUBLIC JOB LISTING — ACTIVE ONLY
    // ====================================================
    @Transactional(readOnly = true)
    public Page<JobDTO> getAllJobs(
            int page,
            int size,
            String sortBy,
            String search,
            String category
    ) {

        Pageable pageable =
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));

        Page<Job> jobs;

        if (search != null && !search.trim().isEmpty()) {
            jobs = jobRepository.searchByStatus(
                    JobStatus.ACTIVE,
                    search.trim(),
                    pageable
            );
        } else if (category != null && !category.trim().isEmpty()) {
            jobs = jobRepository.findByStatusAndCategory(
                    JobStatus.ACTIVE,
                    category.trim(),
                    pageable
            );
        } else {
            jobs = jobRepository.findByStatus(JobStatus.ACTIVE, pageable);
        }

        return jobs.map(jobMapper::toDTO);
    }

    // ====================================================
    // GET JOB BY ID — ACTIVE ONLY
    // ====================================================
    @Transactional(readOnly = true)
    public JobDTO getJobById(UUID id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found")
                );

        if (job.getStatus() != JobStatus.ACTIVE) {
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
                && !user.getUserType().name().equals("JOB_PROVIDER")) {
            throw new AccessDeniedException("Only job providers can post jobs");
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

        JobStatus status =
                user.getRole().name().equals("ADMIN")
                        ? JobStatus.ACTIVE
                        : JobStatus.PENDING_APPROVAL;

        Job job = Job.builder()
                .title(request.getTitle().trim())
                .company(request.getCompany().trim())
                .category(request.getCategory().trim())
                .location(request.getLocation().trim())
                .jobType(jobType)
                .description(request.getDescription())
                .createdBy(user.getId())
                .createdAt(LocalDateTime.now())
                .status(status)
                .build();

        return jobMapper.toDTO(jobRepository.save(job));
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
                .findByStatusOrderByCreatedAtDesc(JobStatus.PENDING_APPROVAL, pageable)
                .map(jobMapper::toDTO);
    }

    // ====================================================
    // ADMIN — APPROVE
    // ====================================================
    @Transactional
    public void approveJob(UUID jobId) {

        User admin = getCurrentUser();

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Only admin allowed");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found")
                );

        job.approve();

        jobRepository.save(job);
    }

    // ====================================================
    // ADMIN — REJECT
    // ====================================================
    @Transactional
    public void rejectJob(UUID jobId) {

        User admin = getCurrentUser();

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Only admin allowed");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found")
                );

        job.reject();

        jobRepository.save(job);
    }

    // ====================================================
    // UPDATE JOB
    // ====================================================
    @Transactional
    public JobDTO updateJob(UUID id, JobUpdateRequest request) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found")
                );

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
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found")
                );

        User user = getCurrentUser();

        if (!user.getRole().name().equals("ADMIN")
                && !job.getCreatedBy().equals(user.getId())) {
            throw new AccessDeniedException("Not allowed");
        }

        jobRepository.delete(job);
    }

    // ====================================================
    // CURRENT USER
    // ====================================================
    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        return userRepository.findById(principal.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );
    }
}
