package com.easyjobspot.backend.job.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class JobDTO {

    private UUID id;

    private String title;
    private String company;
    private String category;
    private String location;
    private String jobType;
    private String description;

    // ✅ THIS FIXES "UNKNOWN"
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime deadline;

    // ===== EXTRA FIELDS =====
    private String workMode;
    private String employmentLevel;

    private Double salaryMin;
    private Double salaryMax;

    private Integer experienceMin;
    private Integer experienceMax;

    private Integer vacancyCount;

    private String applicationType;
    private String applicationUrl;
}
