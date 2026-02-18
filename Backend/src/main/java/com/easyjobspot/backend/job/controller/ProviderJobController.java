package com.easyjobspot.backend.job.controller;

import com.easyjobspot.backend.common.dto.ApiResponse;
import com.easyjobspot.backend.job.dto.JobDTO;
import com.easyjobspot.backend.job.dto.request.JobCreateRequest;
import com.easyjobspot.backend.job.dto.request.JobUpdateRequest;
import com.easyjobspot.backend.job.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/provider/jobs")
@RequiredArgsConstructor
public class ProviderJobController {

    private final JobService jobService;

    // ====================================================
    // GET ALL JOBS BY CURRENT PROVIDER (ALL STATUSES)
    // ====================================================
    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobDTO>>> getMyJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status
    ) {

        Page<JobDTO> jobs =
                jobService.getJobsByCurrentProvider(page, size, status);

        return ResponseEntity.ok(
                ApiResponse.success("Provider jobs fetched successfully", jobs)
        );
    }

    // ====================================================
    // CREATE JOB
    // ====================================================
    @PostMapping
    public ResponseEntity<ApiResponse<JobDTO>> createJob(
            @Valid @RequestBody JobCreateRequest request
    ) {

        JobDTO job = jobService.createJob(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Job submitted successfully and is pending admin approval",
                        job
                )
        );
    }

    // ====================================================
    // UPDATE JOB
    // ====================================================
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobDTO>> updateJob(
            @PathVariable UUID id,
            @Valid @RequestBody JobUpdateRequest request
    ) {

        JobDTO job = jobService.updateJob(id, request);

        return ResponseEntity.ok(
                ApiResponse.success("Job updated successfully", job)
        );
    }

    // ====================================================
    // CLOSE JOB
    // ====================================================
    @PutMapping("/{id}/close")
    public ResponseEntity<ApiResponse<Void>> closeJob(
            @PathVariable UUID id
    ) {

        jobService.closeJob(id);

        return ResponseEntity.ok(
                ApiResponse.success("Job closed successfully", null)
        );
    }

    // ====================================================
    // REOPEN JOB (CLOSED → ACTIVE)
    // ====================================================
    @PutMapping("/{id}/reopen")
    public ResponseEntity<ApiResponse<Void>> reopenJob(
            @PathVariable UUID id
    ) {

        jobService.reopenJob(id);

        return ResponseEntity.ok(
                ApiResponse.success("Job reopened successfully", null)
        );
    }
}
