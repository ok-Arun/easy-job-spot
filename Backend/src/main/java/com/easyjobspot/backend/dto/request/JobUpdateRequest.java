package com.easyjobspot.backend.dto.request;

import jakarta.validation.constraints.NotBlank;

public class JobUpdateRequest {

    @NotBlank(message = "Job title is required")
    private String title;

    @NotBlank(message = "Company name is required")
    private String company;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Job type is required")
    private String jobType;

    @NotBlank(message = "Job description is required")
    private String description;

    public JobUpdateRequest() {
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
}
