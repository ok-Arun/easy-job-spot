package com.easyjobspot.backend.job.controller;

import com.easyjobspot.backend.common.dto.ApiResponse;
import com.easyjobspot.backend.job.dto.JobDTO;
import com.easyjobspot.backend.job.service.JobService;
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
    // VIEW ALL JOBS (ADMIN — ALL STATUSES + FILTER)
    // ====================================================
    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobDTO>>> getAllJobs(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<JobDTO> jobs = jobService.getAllJobsForAdmin(status, page, size);

        return ResponseEntity.ok(
                ApiResponse.success("All jobs fetched successfully", jobs)
        );
    }

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
    // VIEW SINGLE JOB DETAILS
    // ====================================================
    @GetMapping("/{jobId}")
    public ResponseEntity<ApiResponse<JobDTO>> getJobById(
            @PathVariable UUID jobId
    ) {

        JobDTO job = jobService.getJobByIdForAdmin(jobId);

        return ResponseEntity.ok(
                ApiResponse.success("Job fetched successfully", job)
        );
    }

    // ====================================================
    // APPROVE
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
    // REJECT
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
    // CLOSE
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

    // ====================================================
    // REMOVE
    // ====================================================
    @PutMapping("/{jobId}/remove")
    public ResponseEntity<ApiResponse<Void>> removeJob(
            @PathVariable UUID jobId
    ) {

        jobService.removeJobByAdmin(jobId);

        return ResponseEntity.ok(
                ApiResponse.success("Job removed by admin successfully", null)
        );
    }

    // ====================================================
    // RESTORE
    // ====================================================
    @PutMapping("/{jobId}/restore")
    public ResponseEntity<ApiResponse<Void>> restoreJob(
            @PathVariable UUID jobId
    ) {

        jobService.restoreJobByAdmin(jobId);

        return ResponseEntity.ok(
                ApiResponse.success("Job restored by admin successfully", null)
        );
    }
}
