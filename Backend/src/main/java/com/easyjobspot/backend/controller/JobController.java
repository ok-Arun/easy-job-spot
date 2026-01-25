package com.easyjobspot.backend.controller;

import com.easyjobspot.backend.dto.request.JobCreateRequest;
import com.easyjobspot.backend.dto.request.JobUpdateRequest;
import com.easyjobspot.backend.dto.response.ApiResponse;
import com.easyjobspot.backend.dto.response.JobDTO;
import com.easyjobspot.backend.service.JobService;
import jakarta.validation.Valid;
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

    // ================= GET ALL JOBS =================
    // PUBLIC — ACTIVE JOBS ONLY
    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobDTO>>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category
    ) {

        Page<JobDTO> jobs = jobService.getAllJobs(page, size, sortBy, search, category);

        return ResponseEntity.ok(
                ApiResponse.success("Jobs fetched successfully", jobs)
        );
    }

    // ================= GET JOB BY ID =================
    // PUBLIC — ACTIVE ONLY
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobDTO>> getJobById(@PathVariable UUID id) {

        JobDTO job = jobService.getJobById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Job fetched successfully", job)
        );
    }

    // ================= CREATE JOB =================
    // PROVIDER / ADMIN
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

    // ================= UPDATE JOB =================
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

    // ================= DELETE JOB =================
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(@PathVariable UUID id) {

        jobService.deleteJob(id);

        return ResponseEntity.ok(
                ApiResponse.success("Job deleted successfully", null)
        );
    }
}
