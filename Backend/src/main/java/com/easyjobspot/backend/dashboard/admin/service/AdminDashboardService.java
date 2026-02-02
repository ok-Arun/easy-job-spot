package com.easyjobspot.backend.dashboard.admin.service;


import com.easyjobspot.backend.dashboard.admin.dto.AdminDashboardStatsResponse;
import com.easyjobspot.backend.dashboard.admin.dto.JobApplicationCountDTO;
import com.easyjobspot.backend.job.entity.Job;
import com.easyjobspot.backend.job.repository.JobRepository;
import com.easyjobspot.backend.jobapplication.enums.ApplicationStatus;
import com.easyjobspot.backend.jobapplication.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

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
                .perJobApplications(perJobStats)
                .build();
    }
}
