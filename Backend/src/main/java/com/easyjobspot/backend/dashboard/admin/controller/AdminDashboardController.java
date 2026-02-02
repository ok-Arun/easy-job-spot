package com.easyjobspot.backend.dashboard.admin.controller;


import com.easyjobspot.backend.common.dto.ApiResponse;
import com.easyjobspot.backend.dashboard.admin.dto.AdminDashboardStatsResponse;
import com.easyjobspot.backend.dashboard.admin.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats")
    public ApiResponse<AdminDashboardStatsResponse> getDashboardStats() {
        return ApiResponse.success(
                "Dashboard statistics fetched successfully",
                dashboardService.getDashboardStats()
        );
    }
}
