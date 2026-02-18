package com.easyjobspot.backend.dashboard.provider.dto;

import com.easyjobspot.backend.job.entity.Job;

import java.util.UUID;

public class ProviderJobStatsResponse {

    private UUID jobId;
    private String jobTitle;
    private Job.JobStatus status;
    private long totalApplications;
    private long shortlisted;
    private long rejected;
    private long hired;

    public ProviderJobStatsResponse(
            UUID jobId,
            String jobTitle,
            Job.JobStatus status,
            long totalApplications,
            long shortlisted,
            long rejected,
            long hired
    ) {
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.status = status;
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

    public Job.JobStatus getStatus() {
        return status;
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
