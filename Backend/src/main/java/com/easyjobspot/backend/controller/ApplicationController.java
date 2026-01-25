package com.easyjobspot.backend.controller;

import com.easyjobspot.backend.dto.response.ApplicationResponse;
import com.easyjobspot.backend.dto.response.ApiResponse;
import com.easyjobspot.backend.service.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<ApiResponse<ApplicationResponse>> applyToJob(
            @PathVariable UUID jobId
    ) {
        ApplicationResponse response = applicationService.applyToJob(jobId);
        return ResponseEntity.ok(
                ApiResponse.success("Job applied successfully", response)
        );
    }

    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ApplicationResponse>>> getMyApplications() {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Applications fetched successfully",
                        applicationService.getMyApplications()
                )
        );
    }
}
