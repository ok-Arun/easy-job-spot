package com.easyjobspot.backend.dashboard.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminDashboardTrendsResponse {

    private JobTrends jobs;
    private ApplicationTrends applications;
    private HiringFunnelTrends hiringFunnel;

    // ====================================================
    // JOB TRENDS
    // ====================================================
    @Getter
    @Builder
    public static class JobTrends {

        private CreatedJobs created;
        private ApprovedJobs approved;

        @Getter
        @Builder
        public static class CreatedJobs {
            private long last7Days;
            private long last30Days;
        }

        @Getter
        @Builder
        public static class ApprovedJobs {
            private long last7Days;
            private long last30Days;
        }
    }

    // ====================================================
    // APPLICATION TRENDS
    // ====================================================
    @Getter
    @Builder
    public static class ApplicationTrends {

        private ReceivedApplications received;

        @Getter
        @Builder
        public static class ReceivedApplications {
            private long last7Days;
            private long last30Days;
        }
    }

    // ====================================================
    // HIRING FUNNEL (LAST 30 DAYS)
    // ====================================================
    @Getter
    @Builder
    public static class HiringFunnelTrends {

        private long applied;
        private long shortlisted;
        private long hired;

        private double shortlistRate;
        private double hireRate;
    }
}
