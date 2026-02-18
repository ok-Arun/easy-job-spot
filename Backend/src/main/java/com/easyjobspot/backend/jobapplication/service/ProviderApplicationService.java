package com.easyjobspot.backend.jobapplication.service;

import com.easyjobspot.backend.job.entity.Job;
import com.easyjobspot.backend.job.repository.JobRepository;
import com.easyjobspot.backend.jobapplication.dto.response.ProviderApplicationResponse;
import com.easyjobspot.backend.jobapplication.entity.Application;
import com.easyjobspot.backend.jobapplication.repository.ApplicationRepository;
import com.easyjobspot.backend.security.UserPrincipal;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProviderApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;

    // ================= GET APPLICATIONS =================
    public List<ProviderApplicationResponse> getApplicationsForProviderJob(UUID jobId) {

        UUID providerId = getCurrentProviderId();

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (!job.getCreatedBy().equals(providerId)) {
            throw new AccessDeniedException("Not your job");
        }

        return applicationRepository.findApplicationsByJobId(jobId)
                .stream()
                .map(app -> ProviderApplicationResponse.builder()
                        .id(app.getId())
                        .applicantName(app.getUser().getName())
                        .applicantEmail(app.getUser().getEmail())
                        .appliedAt(app.getAppliedAt())
                        .status(app.getStatus())
                        .build())
                .toList();
    }

    // ================= SHORTLIST =================
    public void shortlist(UUID applicationId) {

        Application app = getValidatedApplication(applicationId);
        app.shortlist();

        applicationRepository.save(app);
    }

    // ================= REJECT =================
    public void reject(UUID applicationId) {

        Application app = getValidatedApplication(applicationId);
        app.reject("Rejected by provider");

        applicationRepository.save(app);
    }

    // ================= HIRE =================
    public void hire(UUID applicationId) {

        Application app = getValidatedApplication(applicationId);
        app.hire();

        applicationRepository.save(app);
    }

    // ================= COMMON VALIDATION =================
    private Application getValidatedApplication(UUID applicationId) {

        UUID providerId = getCurrentProviderId();

        Application app = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if (!app.getJob().getCreatedBy().equals(providerId)) {
            throw new AccessDeniedException("Not your application");
        }

        return app;
    }

    private UUID getCurrentProviderId() {

        UserPrincipal principal = (UserPrincipal)
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        return principal.getId();
    }
}
