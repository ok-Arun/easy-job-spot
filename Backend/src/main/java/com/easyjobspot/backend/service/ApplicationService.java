package com.easyjobspot.backend.service;

import com.easyjobspot.backend.dto.response.AdminApplicationResponse;
import com.easyjobspot.backend.dto.response.ApplicationResponse;
import com.easyjobspot.backend.entity.Application;
import com.easyjobspot.backend.entity.Job;
import com.easyjobspot.backend.entity.Job.JobStatus;
import com.easyjobspot.backend.entity.User;
import com.easyjobspot.backend.enums.AuditAction;
import com.easyjobspot.backend.exception.BadRequestException;
import com.easyjobspot.backend.exception.ResourceNotFoundException;
import com.easyjobspot.backend.mapper.ApplicationMapper;
import com.easyjobspot.backend.repository.ApplicationRepository;
import com.easyjobspot.backend.repository.JobRepository;
import com.easyjobspot.backend.repository.UserRepository;
import com.easyjobspot.backend.security.UserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final AuditLogService auditLogService;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            UserRepository userRepository,
            JobRepository jobRepository,
            AuditLogService auditLogService
    ) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.auditLogService = auditLogService;
    }

    // ====================================================
    // APPLY TO JOB — JOB SEEKER ONLY
    // ====================================================
    @Transactional
    public ApplicationResponse applyToJob(UUID jobId) {

        User user = getCurrentUser();

        if (!user.getUserType().name().equals("JOB_SEEKER")) {
            throw new AccessDeniedException("Only job seekers can apply for jobs");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found")
                );

        if (job.getStatus() != JobStatus.ACTIVE) {
            throw new BadRequestException("This job is not accepting applications");
        }

        if (applicationRepository.existsByUserAndJob(user, job)) {
            throw new BadRequestException("You have already applied for this job");
        }

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);

        Application saved = applicationRepository.save(application);

        return ApplicationMapper.toResponse(saved);
    }

    // ====================================================
    // VIEW MY APPLICATIONS — JOB SEEKER ONLY
    // ====================================================
    @Transactional(readOnly = true)
    public List<ApplicationResponse> getMyApplications() {

        User user = getCurrentUser();

        if (!user.getUserType().name().equals("JOB_SEEKER")) {
            throw new AccessDeniedException("Only job seekers can view their applications");
        }

        return applicationRepository.findApplicationsWithJob(user)
                .stream()
                .map(ApplicationMapper::toResponse)
                .collect(Collectors.toList());
    }

    // ====================================================
    // VIEW APPLICANTS — ADMIN OR JOB OWNER
    // ====================================================
    @Transactional(readOnly = true)
    public List<AdminApplicationResponse> getApplicationsForJob(UUID jobId) {

        User user = getCurrentUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found")
                );

        if (user.getRole().name().equals("ADMIN")
                || (user.getUserType().name().equals("JOB_PROVIDER")
                && job.getCreatedBy().equals(user.getId()))) {

            return applicationRepository.findApplicationsByJobId(job.getId())
                    .stream()
                    .map(ApplicationMapper::toAdminResponse)
                    .collect(Collectors.toList());
        }

        throw new AccessDeniedException("You are not allowed to view applications for this job");
    }

    // ====================================================
    // ADMIN — SHORTLIST APPLICATION
    // ====================================================
    @Transactional
    public void shortlistApplication(UUID applicationId) {

        User admin = getCurrentUser();

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Only admin allowed");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Application not found")
                );

        application.shortlist();
        applicationRepository.save(application);

        auditLogService.log(
                admin.getId(),
                AuditAction.APPLICATION_SHORTLISTED,
                application.getId(),
                "Application shortlisted"
        );
    }

    // ====================================================
    // ADMIN — REJECT APPLICATION
    // ====================================================
    @Transactional
    public void rejectApplication(UUID applicationId, String reason) {

        if (reason == null || reason.trim().isEmpty()) {
            throw new BadRequestException("Rejection reason is required");
        }

        User admin = getCurrentUser();

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Only admin allowed");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Application not found")
                );

        application.reject(reason.trim());
        applicationRepository.save(application);

        auditLogService.log(
                admin.getId(),
                AuditAction.APPLICATION_REJECTED,
                application.getId(),
                reason.trim()
        );
    }

    // ====================================================
    // ADMIN — HIRE APPLICATION
    // ====================================================
    @Transactional
    public void hireApplication(UUID applicationId) {

        User admin = getCurrentUser();

        if (!admin.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("Only admin allowed");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Application not found")
                );

        application.hire();
        applicationRepository.save(application);

        auditLogService.log(
                admin.getId(),
                AuditAction.APPLICATION_HIRED,
                application.getId(),
                "Candidate hired"
        );
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
