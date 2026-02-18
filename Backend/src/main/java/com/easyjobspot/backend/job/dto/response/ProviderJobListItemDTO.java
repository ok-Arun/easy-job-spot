package com.easyjobspot.backend.job.dto.response;

import com.easyjobspot.backend.job.entity.Job;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProviderJobListItemDTO {

    private UUID id;
    private String title;
    private String company;
    private String category;
    private String location;
    private Job.JobType jobType;
    private Job.JobStatus status;
    private LocalDateTime createdAt;
    private long totalApplications;

    public ProviderJobListItemDTO(
            UUID id,
            String title,
            String company,
            String category,
            String location,
            Job.JobType jobType,
            Job.JobStatus status,
            LocalDateTime createdAt,
            long totalApplications
    ) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.category = category;
        this.location = location;
        this.jobType = jobType;
        this.status = status;
        this.createdAt = createdAt;
        this.totalApplications = totalApplications;
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getCategory() {
        return category;
    }

    public String getLocation() {
        return location;
    }

    public Job.JobType getJobType() {
        return jobType;
    }

    public Job.JobStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public long getTotalApplications() {
        return totalApplications;
    }
}
