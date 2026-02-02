package com.easyjobspot.backend.job.mapper;

import com.easyjobspot.backend.job.dto.request.JobCreateRequest;
import com.easyjobspot.backend.job.dto.request.JobUpdateRequest;
import com.easyjobspot.backend.job.dto.JobDTO;
import com.easyjobspot.backend.job.entity.Job;
import com.easyjobspot.backend.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    // ENTITY → DTO
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
                .createdAt(job.getCreatedAt())
                .build();
    }

    // CREATE REQUEST → ENTITY
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
                .build();
    }

    // UPDATE REQUEST → ENTITY
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

    // 🔒 SAFE ENUM PARSER
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
