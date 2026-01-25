package com.easyjobspot.backend.controller;

import com.easyjobspot.backend.dto.response.AdminApplicationResponse;
import com.easyjobspot.backend.dto.response.ApiResponse;
import com.easyjobspot.backend.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/jobs")
public class AdminApplicationController {

    private final ApplicationService applicationService;

    public AdminApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{jobId}/applications")
    public ResponseEntity<ApiResponse<List<AdminApplicationResponse>>> getApplicationsForJob(
            @PathVariable UUID jobId
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Applications fetched successfully",
                        applicationService.getApplicationsForJob(jobId)
                )
        );
    }
}
