package com.easyjobspot.backend.job.controller;

import com.easyjobspot.backend.common.dto.ApiResponse;
import com.easyjobspot.backend.job.dto.JobDTO;
import com.easyjobspot.backend.job.entity.Job;
import com.easyjobspot.backend.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    // ====================================================
    // GET ALL JOBS (PUBLIC — ACTIVE ONLY)
    // ====================================================
    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobDTO>>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Job.JobType jobType,
            @RequestParam(required = false) String workMode,
            @RequestParam(required = false) String employmentLevel
    ) {

        Page<JobDTO> jobs =
                jobService.getAllJobs(
                        page,
                        size,
                        sortBy,
                        search,
                        category,
                        title,
                        location,
                        jobType,
                        workMode,
                        employmentLevel
                );

        return ResponseEntity.ok(
                ApiResponse.success("Jobs fetched successfully", jobs)
        );
    }

    // ====================================================
    // GET JOB BY ID (PUBLIC — ACTIVE ONLY)
    // ====================================================
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobDTO>> getJobById(
            @PathVariable UUID id
    ) {

        JobDTO job = jobService.getJobById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Job fetched successfully", job)
        );
    }
}