package com.easyjobspot.backend.jobapplication.dto.response;



import com.easyjobspot.backend.jobapplication.enums.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class ApplicationResponse {

    private UUID id;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;

    private UUID jobId;
    private String jobTitle;
    private String company;
    private String location;
    private String category;

    public ApplicationResponse() {
    }

    public UUID getId() {
        return id;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public UUID getJobId() {
        return jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public void setJobId(UUID jobId) {
        this.jobId = jobId;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
