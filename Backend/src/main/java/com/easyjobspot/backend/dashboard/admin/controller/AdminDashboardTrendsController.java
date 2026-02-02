package com.easyjobspot.backend.dashboard.admin.controller;

import com.easyjobspot.backend.common.dto.ApiResponse;
import com.easyjobspot.backend.dashboard.admin.dto.AdminDashboardTrendsResponse;
import com.easyjobspot.backend.dashboard.admin.service.AdminDashboardTrendsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardTrendsController {

    private final AdminDashboardTrendsService trendsService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/trends")
    public ApiResponse<AdminDashboardTrendsResponse> getDashboardTrends() {
        return ApiResponse.success(
                "Dashboard trends fetched successfully",
                trendsService.getTrends()
        );
    }
}
