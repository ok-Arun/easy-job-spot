package com.easyjobspot.backend.controller;

import com.easyjobspot.backend.dto.response.JobDTO;
import com.easyjobspot.backend.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
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
    public ResponseEntity<Page<JobDTO>> getPendingJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(jobService.getPendingJobs(page, size));
    }

    // ====================================================
    // APPROVE JOB
    // ====================================================
    @PutMapping("/{jobId}/approve")
    public ResponseEntity<Map<String, Object>> approveJob(
            @PathVariable UUID jobId
    ) {
        jobService.approveJob(jobId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Job approved successfully");
        response.put("data", null);

        return ResponseEntity.ok(response);
    }

    // ====================================================
    // REJECT JOB
    // ====================================================
    @PutMapping("/{jobId}/reject")
    public ResponseEntity<Map<String, Object>> rejectJob(
            @PathVariable UUID jobId
    ) {
        jobService.rejectJob(jobId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Job rejected successfully");
        response.put("data", null);

        return ResponseEntity.ok(response);
    }
}
