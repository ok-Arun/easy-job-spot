package com.easyjobspot.backend.dashboard.admin.service;


import com.easyjobspot.backend.dashboard.admin.dto.AdminDashboardTrendsResponse;
import com.easyjobspot.backend.job.entity.Job;
import com.easyjobspot.backend.job.repository.JobRepository;
import com.easyjobspot.backend.jobapplication.enums.ApplicationStatus;
import com.easyjobspot.backend.jobapplication.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminDashboardTrendsService {

    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public AdminDashboardTrendsResponse getTrends() {

        // ================= TIME WINDOWS =================
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime last7Days = now.minusDays(7);
        LocalDateTime last30Days = now.minusDays(30);

        // ================= JOB TRENDS =================
        long jobsCreatedLast7Days =
                jobRepository.countByCreatedAtAfter(last7Days);

        long jobsCreatedLast30Days =
                jobRepository.countByCreatedAtAfter(last30Days);

        long jobsApprovedLast7Days =
                jobRepository.countByStatusAndCreatedAtAfter(
                        Job.JobStatus.ACTIVE,
                        last7Days
                );

        long jobsApprovedLast30Days =
                jobRepository.countByStatusAndCreatedAtAfter(
                        Job.JobStatus.ACTIVE,
                        last30Days
                );

        // ================= APPLICATION TRENDS =================
        long applicationsLast7Days =
                applicationRepository.countByAppliedAtAfter(last7Days);

        long applicationsLast30Days =
                applicationRepository.countByAppliedAtAfter(last30Days);

        // ================= HIRING FUNNEL (LAST 30 DAYS) =================
        long applied =
                applicationRepository.countByAppliedAtAfter(last30Days);

        long shortlisted =
                applicationRepository.countByStatusAndAppliedAtAfter(
                        ApplicationStatus.SHORTLISTED,
                        last30Days
                );

        long hired =
                applicationRepository.countByStatusAndAppliedAtAfter(
                        ApplicationStatus.HIRED,
                        last30Days
                );

        double shortlistRate =
                applied == 0 ? 0.0 : (double) shortlisted / applied;

        double hireRate =
                applied == 0 ? 0.0 : (double) hired / applied;

        // ================= BUILD RESPONSE =================
        return AdminDashboardTrendsResponse.builder()
                .jobs(
                        AdminDashboardTrendsResponse.JobTrends.builder()
                                .created(
                                        AdminDashboardTrendsResponse.JobTrends.CreatedJobs.builder()
                                                .last7Days(jobsCreatedLast7Days)
                                                .last30Days(jobsCreatedLast30Days)
                                                .build()
                                )
                                .approved(
                                        AdminDashboardTrendsResponse.JobTrends.ApprovedJobs.builder()
                                                .last7Days(jobsApprovedLast7Days)
                                                .last30Days(jobsApprovedLast30Days)
                                                .build()
                                )
                                .build()
                )
                .applications(
                        AdminDashboardTrendsResponse.ApplicationTrends.builder()
                                .received(
                                        AdminDashboardTrendsResponse.ApplicationTrends.ReceivedApplications.builder()
                                                .last7Days(applicationsLast7Days)
                                                .last30Days(applicationsLast30Days)
                                                .build()
                                )
                                .build()
                )
                .hiringFunnel(
                        AdminDashboardTrendsResponse.HiringFunnelTrends.builder()
                                .applied(applied)
                                .shortlisted(shortlisted)
                                .hired(hired)
                                .shortlistRate(shortlistRate)
                                .hireRate(hireRate)
                                .build()
                )
                .build();
    }
}
