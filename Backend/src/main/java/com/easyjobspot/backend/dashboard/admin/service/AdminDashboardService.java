package com.easyjobspot.backend.dashboard.admin.service;

import com.easyjobspot.backend.dashboard.admin.dto.AdminDashboardStatsResponse;
import com.easyjobspot.backend.dashboard.admin.dto.JobApplicationCountDTO;
import com.easyjobspot.backend.dashboard.admin.dto.AdminJobApplicationView;
import com.easyjobspot.backend.job.entity.Job;
import com.easyjobspot.backend.job.repository.JobRepository;
import com.easyjobspot.backend.jobapplication.enums.ApplicationStatus;
import com.easyjobspot.backend.jobapplication.repository.ApplicationRepository;
import com.easyjobspot.backend.user.entity.User;
import com.easyjobspot.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    public AdminDashboardStatsResponse getDashboardStats() {

        // ================= JOB STATS =================
        long totalJobs = jobRepository.count();
        long pendingJobs = jobRepository.countByStatus(Job.JobStatus.PENDING_APPROVAL);
        long activeJobs = jobRepository.countByStatus(Job.JobStatus.ACTIVE);

        // ================= APPLICATION STATS =================
        long totalApplications = applicationRepository.count();
        long shortlisted = applicationRepository.countByStatus(ApplicationStatus.SHORTLISTED);
        long rejected = applicationRepository.countByStatus(ApplicationStatus.REJECTED);
        long hired = applicationRepository.countByStatus(ApplicationStatus.HIRED);

        // ================= USER STATS =================
        long totalUsers = userRepository.count();

        long jobSeekers =
                userRepository.countByUserType(User.UserType.JOB_SEEKER);

        long providers =
                userRepository.countByUserType(User.UserType.JOB_PROVIDER);

        long admins =
                userRepository.countByRole(User.Role.ADMIN);

        long pendingProviders =
                userRepository.countByUserTypeAndProviderStatus(
                        User.UserType.JOB_PROVIDER,
                        User.ProviderStatus.PENDING
                );

        AdminDashboardStatsResponse.UserStats userStats =
                AdminDashboardStatsResponse.UserStats.builder()
                        .total(totalUsers)
                        .jobSeekers(jobSeekers)
                        .providers(providers)
                        .admins(admins)
                        .pendingProviders(pendingProviders)
                        .build();

        // ================= PER JOB COUNTS =================
        List<JobApplicationCountDTO> perJobStats =
                applicationRepository.countApplicationsPerJob()
                        .stream()
                        .map(row -> new JobApplicationCountDTO(
                                (UUID) row[0],
                                (String) row[1],
                                (Long) row[2]
                        ))
                        .toList();

        return AdminDashboardStatsResponse.builder()
                .jobs(
                        AdminDashboardStatsResponse.JobStats.builder()
                                .total(totalJobs)
                                .pending(pendingJobs)
                                .active(activeJobs)
                                .build()
                )
                .applications(
                        AdminDashboardStatsResponse.ApplicationStats.builder()
                                .total(totalApplications)
                                .shortlisted(shortlisted)
                                .rejected(rejected)
                                .hired(hired)
                                .build()
                )
                .users(userStats)
                .perJobApplications(perJobStats)
                .build();
    }

    // ====================================================
    // ADMIN — VIEW APPLICATIONS PER JOB (READ ONLY)
    // ====================================================

    public List<AdminJobApplicationView> getApplicationsForJob(UUID jobId) {

        return applicationRepository.findApplicationsByJobId(jobId)
                .stream()
                .map(app -> AdminJobApplicationView.builder()
                        .applicationId(app.getId())
                        .applicantId(app.getUser().getId())
                        .applicantName(app.getUser().getName())
                        .applicantEmail(app.getUser().getEmail())
                        .status(app.getStatus())
                        .appliedAt(app.getAppliedAt())
                        .build()
                )
                .toList();
    }
}
