package com.easyjobspot.backend.jobapplication.service;

import com.easyjobspot.backend.audit.enums.AuditAction;
import com.easyjobspot.backend.audit.service.AuditLogService;
import com.easyjobspot.backend.exception.BadRequestException;
import com.easyjobspot.backend.exception.ResourceNotFoundException;
import com.easyjobspot.backend.job.entity.Job;
import com.easyjobspot.backend.job.repository.JobRepository;
import com.easyjobspot.backend.jobapplication.dto.response.AdminApplicationResponse;
import com.easyjobspot.backend.jobapplication.dto.response.ApplicationResponse;
import com.easyjobspot.backend.jobapplication.entity.Application;
import com.easyjobspot.backend.jobapplication.mapper.ApplicationMapper;
import com.easyjobspot.backend.jobapplication.repository.ApplicationRepository;
import com.easyjobspot.backend.security.UserPrincipal;
import com.easyjobspot.backend.user.entity.User;
import com.easyjobspot.backend.user.repository.UserRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        if (job.getStatus() != Job.JobStatus.ACTIVE) {
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
    // VIEW APPLICATIONS FOR JOB — ADMIN (AUDIT) OR PROVIDER (OWNER)
    // ====================================================
    @Transactional(readOnly = true)
    public List<AdminApplicationResponse> getApplicationsForJob(UUID jobId) {

        User user = getCurrentUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        boolean isAdmin = user.getRole().name().equals("ADMIN");
        boolean isProviderOwner =
                user.getUserType().name().equals("JOB_PROVIDER")
                        && job.getCreatedBy().equals(user.getId());

        if (!isAdmin && !isProviderOwner) {
            throw new AccessDeniedException("You are not allowed to view applications for this job");
        }

        return applicationRepository.findApplicationsByJobId(job.getId())
                .stream()
                .map(ApplicationMapper::toAdminResponse)
                .collect(Collectors.toList());
    }

    // ====================================================
    // PROVIDER — SHORTLIST APPLICATION
    // ====================================================
    @Transactional
    public void shortlistApplication(UUID applicationId) {

        User provider = getCurrentUser();

        Application application = getApplicationForProviderAction(applicationId, provider);

        application.shortlist();
        applicationRepository.save(application);

        auditLogService.log(
                provider.getId(),
                AuditAction.APPLICATION_SHORTLISTED,
                application.getId(),
                "Application shortlisted by provider"
        );
    }

    // ====================================================
    // PROVIDER — REJECT APPLICATION
    // ====================================================
    @Transactional
    public void rejectApplication(UUID applicationId, String reason) {

        if (reason == null || reason.trim().isEmpty()) {
            throw new BadRequestException("Rejection reason is required");
        }

        User provider = getCurrentUser();

        Application application = getApplicationForProviderAction(applicationId, provider);

        application.reject(reason.trim());
        applicationRepository.save(application);

        auditLogService.log(
                provider.getId(),
                AuditAction.APPLICATION_REJECTED,
                application.getId(),
                reason.trim()
        );
    }

    // ====================================================
    // PROVIDER — HIRE APPLICATION
    // ====================================================
    @Transactional
    public void hireApplication(UUID applicationId) {

        User provider = getCurrentUser();

        Application application = getApplicationForProviderAction(applicationId, provider);

        application.hire();
        applicationRepository.save(application);

        auditLogService.log(
                provider.getId(),
                AuditAction.APPLICATION_HIRED,
                application.getId(),
                "Candidate hired by provider"
        );
    }

    // ====================================================
    // VALIDATE PROVIDER OWNERSHIP
    // ====================================================
    private Application getApplicationForProviderAction(UUID applicationId, User provider) {

        if (!provider.getUserType().name().equals("JOB_PROVIDER")) {
            throw new AccessDeniedException("Only job providers can perform this action");
        }

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));

        Job job = application.getJob();

        if (!job.getCreatedBy().equals(provider.getId())) {
            throw new AccessDeniedException("You do not own this job");
        }

        return application;
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
