package com.easyjobspot.backend.dashboard.admin.controller;

import com.easyjobspot.backend.common.dto.ApiResponse;
import com.easyjobspot.backend.dashboard.admin.dto.AdminDashboardStatsResponse;
import com.easyjobspot.backend.dashboard.admin.dto.AdminJobApplicationView;
import com.easyjobspot.backend.dashboard.admin.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    // ================= DASHBOARD STATS =================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public ApiResponse<AdminDashboardStatsResponse> getDashboardStats() {
        return ApiResponse.success(
                "Dashboard statistics fetched successfully",
                dashboardService.getDashboardStats()
        );
    }

    // ================= ADMIN — VIEW APPLICATIONS PER JOB =================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/jobs/{jobId}/applications")
    public ApiResponse<List<AdminJobApplicationView>> getApplicationsForJob(
            @PathVariable UUID jobId) {

        return ApiResponse.success(
                "Applications fetched successfully",
                dashboardService.getApplicationsForJob(jobId)
        );
    }
}
