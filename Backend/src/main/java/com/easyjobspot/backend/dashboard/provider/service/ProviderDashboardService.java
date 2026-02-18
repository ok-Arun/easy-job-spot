package com.easyjobspot.backend.dashboard.provider.service;

import com.easyjobspot.backend.dashboard.provider.dto.ProviderDashboardStatsResponse;
import com.easyjobspot.backend.dashboard.provider.dto.ProviderJobStatsResponse;
import com.easyjobspot.backend.job.entity.Job;
import com.easyjobspot.backend.job.repository.JobRepository;
import com.easyjobspot.backend.jobapplication.enums.ApplicationStatus;
import com.easyjobspot.backend.jobapplication.repository.ApplicationRepository;
import com.easyjobspot.backend.security.UserPrincipal;
import com.easyjobspot.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProviderDashboardService {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public ProviderDashboardStatsResponse getDashboardStats() {

        // ================= AUTH CONTEXT =================
        UserPrincipal principal = (UserPrincipal)
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        // ================= ROLE VALIDATION =================
        if (principal.getUser().getUserType() != User.UserType.JOB_PROVIDER) {
            throw new AccessDeniedException(
                    "Only job providers can access dashboard statistics"
            );
        }

        UUID providerId = principal.getId();

        // ================= JOB STATS =================
        long totalJobs = jobRepository.countByCreatedBy(providerId);
        long activeJobs = jobRepository.countByStatusAndCreatedBy(
                Job.JobStatus.ACTIVE, providerId
        );
        long pendingJobs = jobRepository.countByStatusAndCreatedBy(
                Job.JobStatus.PENDING_APPROVAL, providerId
        );

        ProviderDashboardStatsResponse.JobStats jobStats =
                new ProviderDashboardStatsResponse.JobStats(
                        totalJobs,
                        activeJobs,
                        pendingJobs
                );

        // ================= APPLICATION STATS =================
        long totalApplications =
                applicationRepository.countAllByProvider(providerId);

        long shortlisted =
                applicationRepository.countByStatusAndProvider(
                        ApplicationStatus.SHORTLISTED, providerId
                );

        long hired =
                applicationRepository.countByStatusAndProvider(
                        ApplicationStatus.HIRED, providerId
                );

        ProviderDashboardStatsResponse.ApplicationStats applicationStats =
                new ProviderDashboardStatsResponse.ApplicationStats(
                        totalApplications,
                        shortlisted,
                        hired
                );

        // ================= PER-JOB STATS =================
        List<ProviderJobStatsResponse> perJobStats =
                applicationRepository.fetchPerJobApplicationStats(providerId)
                        .stream()
                        .map(row -> new ProviderJobStatsResponse(
                                (UUID) row[0],
                                (String) row[1],
                                (Job.JobStatus) row[2],
                                (Long) row[3],
                                (Long) row[4],
                                (Long) row[5],
                                (Long) row[6]
                        ))
                        .toList();

        // ================= FINAL RESPONSE =================
        return new ProviderDashboardStatsResponse(
                jobStats,
                applicationStats,
                perJobStats
        );
    }
}
