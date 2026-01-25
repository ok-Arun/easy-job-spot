package com.easyjobspot.backend.service;

import com.easyjobspot.backend.dto.response.AdminApplicationResponse;
import com.easyjobspot.backend.dto.response.ApplicationResponse;
import com.easyjobspot.backend.entity.Application;
import com.easyjobspot.backend.entity.Job;
import com.easyjobspot.backend.entity.Job.JobStatus;
import com.easyjobspot.backend.entity.User;
import com.easyjobspot.backend.enums.ApplicationStatus;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            UserRepository userRepository,
            JobRepository jobRepository
    ) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    // ====================================================
    // APPLY TO JOB — JOB SEEKER ONLY
    // ====================================================
    public ApplicationResponse applyToJob(UUID jobId) {

        User user = getCurrentUser();

        if (!user.getUserType().name().equals("JOB_SEEKER")) {
            throw new AccessDeniedException("Only job seekers can apply for jobs");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found")
                );

        // ✅ STATUS VALIDATION
        if (job.getStatus() != JobStatus.ACTIVE) {
            throw new AccessDeniedException("This job is not accepting applications");
        }

        if (applicationRepository.existsByUserAndJob(user, job)) {
            throw new AccessDeniedException("You have already applied for this job");
        }

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.APPLIED);
        application.setAppliedAt(LocalDateTime.now());

        Application saved = applicationRepository.save(application);

        return ApplicationMapper.toResponse(saved);
    }

    // ====================================================
    // VIEW MY APPLICATIONS — JOB SEEKER ONLY
    // ====================================================
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
    public List<AdminApplicationResponse> getApplicationsForJob(UUID jobId) {

        User user = getCurrentUser();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Job not found")
                );

        // ADMIN override
        if (user.getRole().name().equals("ADMIN")) {
            return applicationRepository.findApplicationsByJobId(job.getId())
                    .stream()
                    .map(ApplicationMapper::toAdminResponse)
                    .collect(Collectors.toList());
        }

        // JOB PROVIDER ownership
        if (user.getUserType().name().equals("JOB_PROVIDER")
                && job.getCreatedBy().equals(user.getId())) {

            return applicationRepository.findApplicationsByJobId(job.getId())
                    .stream()
                    .map(ApplicationMapper::toAdminResponse)
                    .collect(Collectors.toList());
        }

        throw new AccessDeniedException("You are not allowed to view applications for this job");
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
