package com.easyjobspot.backend.controller;

import com.easyjobspot.backend.dto.response.ApiResponse;
import com.easyjobspot.backend.dto.response.JobDTO;
import com.easyjobspot.backend.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/jobs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminJobController {

    private final JobService jobService;

    // ====================================================
    // VIEW ALL PENDING JOBS
    // ====================================================
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<Page<JobDTO>>> getPendingJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<JobDTO> jobs = jobService.getPendingJobs(page, size);

        return ResponseEntity.ok(
                ApiResponse.success("Pending jobs fetched successfully", jobs)
        );
    }

    // ====================================================
    // APPROVE JOB
    // ====================================================
    @PutMapping("/{jobId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveJob(
            @PathVariable UUID jobId
    ) {

        jobService.approveJob(jobId);

        return ResponseEntity.ok(
                ApiResponse.success("Job approved successfully", null)
        );
    }

    // ====================================================
    // REJECT JOB
    // ====================================================
    @PutMapping("/{jobId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectJob(
            @PathVariable UUID jobId,
            @RequestParam String reason
    ) {

        jobService.rejectJob(jobId, reason);

        return ResponseEntity.ok(
                ApiResponse.success("Job rejected successfully", null)
        );
    }

    // ====================================================
    // CLOSE JOB (MANUAL STOP HIRING)
    // ====================================================
    @PutMapping("/{jobId}/close")
    public ResponseEntity<ApiResponse<Void>> closeJob(
            @PathVariable UUID jobId
    ) {

        jobService.closeJob(jobId);

        return ResponseEntity.ok(
                ApiResponse.success("Job closed successfully", null)
        );
    }
}
