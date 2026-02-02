package com.easyjobspot.backend.dashboard.provider.dto;

import java.util.UUID;

public class ProviderJobStatsResponse {

    private UUID jobId;
    private String jobTitle;
    private long totalApplications;
    private long shortlisted;
    private long rejected;
    private long hired;

    public ProviderJobStatsResponse(
            UUID jobId,
            String jobTitle,
            long totalApplications,
            long shortlisted,
            long rejected,
            long hired
    ) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.totalApplications = totalApplications;
        this.shortlisted = shortlisted;
        this.rejected = rejected;
        this.hired = hired;
    }

    public UUID getJobId() {
        return jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public long getTotalApplications() {
        return totalApplications;
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
