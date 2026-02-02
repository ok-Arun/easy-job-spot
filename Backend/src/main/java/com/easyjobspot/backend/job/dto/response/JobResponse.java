package com.easyjobspot.backend.job.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class JobResponse {

    private UUID id;
    private String title;
    private String company;
    private String category;
    private String location;
    private String jobType;
    private String description;
    private LocalDateTime createdAt;

    public JobResponse(
            UUID id,
            String title,
            String company,
            String category,
            String location,
            String jobType,
            String description,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.company = company;
        this.category = category;
        this.location = location;
        this.jobType = jobType;
        this.description = description;
        this.createdAt = createdAt;
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

    public String getJobType() {
        return jobType;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
