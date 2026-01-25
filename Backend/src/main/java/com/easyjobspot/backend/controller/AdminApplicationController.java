package com.easyjobspot.backend.controller;

import com.easyjobspot.backend.dto.response.AdminApplicationResponse;
import com.easyjobspot.backend.dto.response.ApiResponse;
import com.easyjobspot.backend.service.ApplicationService;
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
    // VIEW APPLICATIONS FOR JOB
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

    // ====================================================
    // SHORTLIST APPLICATION
    // ====================================================
    @PutMapping("/applications/{applicationId}/shortlist")
    public ResponseEntity<ApiResponse<Void>> shortlistApplication(
            @PathVariable UUID applicationId
    ) {

        applicationService.shortlistApplication(applicationId);

        return ResponseEntity.ok(
                ApiResponse.success("Application shortlisted successfully", null)
        );
    }

    // ====================================================
    // REJECT APPLICATION
    // ====================================================
    @PutMapping("/applications/{applicationId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectApplication(
            @PathVariable UUID applicationId,
            @RequestParam String reason
    ) {

        applicationService.rejectApplication(applicationId, reason);

        return ResponseEntity.ok(
                ApiResponse.success("Application rejected successfully", null)
        );
    }

    // ====================================================
    // HIRE APPLICATION
    // ====================================================
    @PutMapping("/applications/{applicationId}/hire")
    public ResponseEntity<ApiResponse<Void>> hireApplication(
            @PathVariable UUID applicationId
    ) {

        applicationService.hireApplication(applicationId);

        return ResponseEntity.ok(
                ApiResponse.success("Candidate hired successfully", null)
        );
    }
}
