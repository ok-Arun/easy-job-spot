package com.easyjobspot.backend.jobapplication.controller;

import com.easyjobspot.backend.common.dto.ApiResponse;
import com.easyjobspot.backend.jobapplication.dto.response.ProviderApplicationResponse;
import com.easyjobspot.backend.jobapplication.service.ProviderApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/provider")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ProviderApplicationController {

    private final ProviderApplicationService service;

    // ================= GET APPLICATIONS =================
    @GetMapping("/jobs/{jobId}/applications")
    public ResponseEntity<ApiResponse<List<ProviderApplicationResponse>>>
    getApplications(@PathVariable UUID jobId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Applications fetched successfully",
                        service.getApplicationsForProviderJob(jobId)
                )
        );
    }

    // ================= SHORTLIST =================
    @PutMapping("/applications/{applicationId}/shortlist")
    public ResponseEntity<ApiResponse<Void>> shortlist(
            @PathVariable UUID applicationId) {

        service.shortlist(applicationId);

        return ResponseEntity.ok(
                ApiResponse.success("Application shortlisted", null)
        );
    }

    // ================= REJECT =================
    @PutMapping("/applications/{applicationId}/reject")
    public ResponseEntity<ApiResponse<Void>> reject(
            @PathVariable UUID applicationId) {

        service.reject(applicationId);

        return ResponseEntity.ok(
                ApiResponse.success("Application rejected", null)
        );
    }

    // ================= HIRE =================
    @PutMapping("/applications/{applicationId}/hire")
    public ResponseEntity<ApiResponse<Void>> hire(
            @PathVariable UUID applicationId) {

        service.hire(applicationId);

        return ResponseEntity.ok(
                ApiResponse.success("Application hired", null)
        );
    }
}
