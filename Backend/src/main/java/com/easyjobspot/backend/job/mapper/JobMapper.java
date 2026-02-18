package com.easyjobspot.backend.job.mapper;

import com.easyjobspot.backend.job.dto.JobDTO;
import com.easyjobspot.backend.job.dto.request.JobCreateRequest;
import com.easyjobspot.backend.job.dto.request.JobUpdateRequest;
import com.easyjobspot.backend.job.entity.Job;
import com.easyjobspot.backend.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    // ====================================================
    // ENTITY → DTO
    // ====================================================
    public JobDTO toDTO(Job job) {
        if (job == null) {
            return null;
        }

        return JobDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .company(job.getCompany())
                .category(job.getCategory())
                .location(job.getLocation())
                .jobType(job.getJobType().name())
                .description(job.getDescription())

                // ✅ CRITICAL LINE (NO MORE UNKNOWN)
                .status(job.getStatus().name())

                .createdAt(job.getCreatedAt())
                .deadline(job.getDeadline())

                // ===== EXTRA FIELDS =====
                .workMode(job.getWorkMode())
                .employmentLevel(job.getEmploymentLevel())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .experienceMin(job.getExperienceMin())
                .experienceMax(job.getExperienceMax())
                .vacancyCount(job.getVacancyCount())
                .applicationType(job.getApplicationType())
                .applicationUrl(job.getApplicationUrl())
                // ========================

                .build();
    }

    // ====================================================
    // CREATE REQUEST → ENTITY
    // ====================================================
    public Job toEntity(JobCreateRequest request) {
        if (request == null) {
            return null;
        }

        return Job.builder()
                .title(request.getTitle())
                .company(request.getCompany())
                .category(request.getCategory())
                .location(request.getLocation())
                .jobType(parseJobType(request.getJobType()))
                .description(request.getDescription())

                .workMode(request.getWorkMode())
                .employmentLevel(request.getEmploymentLevel())
                .salaryMin(request.getSalaryMin())
                .salaryMax(request.getSalaryMax())
                .experienceMin(request.getExperienceMin())
                .experienceMax(request.getExperienceMax())
                .vacancyCount(request.getVacancyCount())
                .applicationType(request.getApplicationType())
                .applicationUrl(request.getApplicationUrl())
                .deadline(request.getDeadline())

                .build();
    }

    // ====================================================
    // UPDATE REQUEST → ENTITY
    // ====================================================
    public void updateEntity(JobUpdateRequest request, Job job) {
        if (request == null || job == null) {
            return;
        }

        job.update(
                request.getTitle(),
                request.getCompany(),
                request.getCategory(),
                request.getLocation(),
                parseJobType(request.getJobType()),
                request.getDescription()
        );
    }

    // ====================================================
    // SAFE ENUM PARSER
    // ====================================================
    private Job.JobType parseJobType(String value) {
        try {
            return Job.JobType.valueOf(value.trim().toUpperCase());
        } catch (Exception ex) {
            throw new BadRequestException(
                    "Invalid jobType. Allowed values: FULL_TIME, PART_TIME, INTERNSHIP, CONTRACT"
            );
        }
    }
}
