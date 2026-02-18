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

    // ================= JOB STATS =================
    public static class JobStats {
        private long total;
        private long active;
        private long pendingApproval;

        public JobStats(long total, long active, long pendingApproval) {
            this.total = total;
            this.active = active;
            this.pendingApproval = pendingApproval;
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
    }

    // ================= APPLICATION STATS =================
    public static class ApplicationStats {
        private long total;
        private long shortlisted;
        private long hired;

        public ApplicationStats(long total, long shortlisted, long hired) {
            this.total = total;
            this.shortlisted = shortlisted;
            this.hired = hired;
        }

        public long getTotal() {
            return total;
        }

        public long getShortlisted() {
            return shortlisted;
        }

        public long getHired() {
            return hired;
        }
    }
}
