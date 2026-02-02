package com.easyjobspot.backend.dashboard.provider.dto;

import java.util.List;

public class ProviderDashboardStatsResponse {

    private JobStats jobs;
    private ApplicationStats applications;
    private List<ProviderJobStatsResponse> perJob;

    public ProviderDashboardStatsResponse(
            JobStats jobs,
            ApplicationStats applications,
            List<ProviderJobStatsResponse> perJob
    ) {
        this.jobs = jobs;
        this.applications = applications;
        this.perJob = perJob;
    }

    public JobStats getJobs() {
        return jobs;
    }

    public ApplicationStats getApplications() {
        return applications;
    }

    public List<ProviderJobStatsResponse> getPerJob() {
        return perJob;
    }

    // ====================================================
    // INNER RESPONSE MODELS
    // ====================================================

    public static class JobStats {
        private long total;
        private long active;
        private long pendingApproval;
        private long closed;
        private long expired;

        public JobStats(
                long total,
                long active,
                long pendingApproval,
                long closed,
                long expired
        ) {
            this.total = total;
            this.active = active;
            this.pendingApproval = pendingApproval;
            this.closed = closed;
            this.expired = expired;
        }

        public long getTotal() {
            return total;
        }

        public long getActive() {
            return active;
        }

        public long getPendingApproval() {
            return pendingApproval;
        }

        public long getClosed() {
            return closed;
        }

        public long getExpired() {
            return expired;
        }
    }

    public static class ApplicationStats {
        private long total;
        private long shortlisted;
        private long rejected;
        private long hired;

        public ApplicationStats(
                long total,
                long shortlisted,
                long rejected,
                long hired
        ) {
            this.total = total;
            this.shortlisted = shortlisted;
            this.rejected = rejected;
            this.hired = hired;
        }

        public long getTotal() {
            return total;
        }

        public long getShortlisted() {
            return shortlisted;
        }

        public long getRejected() {
            return rejected;
        }

        public long getHired() {
            return hired;
        }
    }
}
