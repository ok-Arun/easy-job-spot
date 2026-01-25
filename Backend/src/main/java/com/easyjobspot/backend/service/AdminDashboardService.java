package com.easyjobspot.backend.service;

import com.easyjobspot.backend.dto.response.AdminDashboardStatsResponse;
import com.easyjobspot.backend.dto.response.JobApplicationCountDTO;
import com.easyjobspot.backend.entity.Job;
import com.easyjobspot.backend.enums.ApplicationStatus;
import com.easyjobspot.backend.repository.ApplicationRepository;
import com.easyjobspot.backend.repository.JobRepository;
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
