package com.easyjobspot.backend.job.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class JobCreateRequest {

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

    // ======== NEW FIELDS ========

    private String workMode;
    private String employmentLevel;

    private Double salaryMin;
    private Double salaryMax;

    private Integer experienceMin;
    private Integer experienceMax;

    private Integer vacancyCount;

    private String applicationType;
    private String applicationUrl;

    // ============================

    @NotNull(message = "Job deadline is required")
    private LocalDateTime deadline;

    public JobCreateRequest() {}

    public String getTitle() { return title; }
    public String getCompany() { return company; }
    public String getCategory() { return category; }
    public String getLocation() { return location; }
    public String getJobType() { return jobType; }
    public String getDescription() { return description; }
    public LocalDateTime getDeadline() { return deadline; }

    public String getWorkMode() { return workMode; }
    public String getEmploymentLevel() { return employmentLevel; }
    public Double getSalaryMin() { return salaryMin; }
    public Double getSalaryMax() { return salaryMax; }
    public Integer getExperienceMin() { return experienceMin; }
    public Integer getExperienceMax() { return experienceMax; }
    public Integer getVacancyCount() { return vacancyCount; }
    public String getApplicationType() { return applicationType; }
    public String getApplicationUrl() { return applicationUrl; }
}
