package com.easyjobspot.backend.dashboard.admin.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AdminDashboardStatsResponse {

    private JobStats jobs;
    private ApplicationStats applications;
    private List<JobApplicationCountDTO> perJobApplications;

    private UserStats users;

    @Getter
    @Builder
    public static class UserStats {
        private long total;
        private long jobSeekers;
        private long providers;
        private long admins;
        private long pendingProviders;
    }

    @Getter
    @Builder
    public static class JobStats {
        private long total;
        private long pending;
        private long active;
    }

    @Getter
    @Builder
    public static class ApplicationStats {
        private long total;
        private long shortlisted;
        private long rejected;
        private long hired;
    }
}
