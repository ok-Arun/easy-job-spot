package com.easyjobspot.backend.jobapplication.controller;

import com.easyjobspot.backend.common.dto.ApiResponse;
import com.easyjobspot.backend.jobapplication.dto.response.AdminApplicationResponse;
import com.easyjobspot.backend.jobapplication.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/jobs")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminApplicationController {

    private final ApplicationService applicationService;

    // ====================================================
    // VIEW APPLICATIONS FOR A JOB (ADMIN - READ ONLY)
    // ====================================================
    @GetMapping("/{jobId}/applications")
    public ResponseEntity<ApiResponse<List<AdminApplicationResponse>>> getApplicationsForJob(
            @PathVariable UUID jobId
    ) {

        List<AdminApplicationResponse> applications =
                applicationService.getApplicationsForJob(jobId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Applications fetched successfully",
                        applications
                )
        );
    }
}
