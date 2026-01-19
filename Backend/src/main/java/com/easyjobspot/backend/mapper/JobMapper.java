package com.easyjobspot.backend.mapper;

import com.easyjobspot.backend.dto.JobDTO;
import com.easyjobspot.backend.dto.JobRequest;
import com.easyjobspot.backend.entity.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

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
                .jobType(job.getJobType())
                .description(job.getDescription())
                .createdAt(job.getCreatedAt())
                .build();
    }

    public Job toEntity(JobRequest request) {
        if (request == null) {
            return null;
        }

        return Job.builder()
                .title(request.getTitle())
                .company(request.getCompany())
                .category(request.getCategory())
                .location(request.getLocation())
                .jobType(request.getJobType())
                .description(request.getDescription())
                .build();
    }

    public void updateEntity(JobRequest request, Job job) {
        if (request == null || job == null) {
            return;
        }

        job.setTitle(request.getTitle());
        job.setCompany(request.getCompany());
        job.setCategory(request.getCategory());
        job.setLocation(request.getLocation());
        job.setJobType(request.getJobType());
        job.setDescription(request.getDescription());
    }
}