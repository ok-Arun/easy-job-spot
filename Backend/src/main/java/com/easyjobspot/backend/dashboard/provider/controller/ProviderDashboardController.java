package com.easyjobspot.backend.dashboard.provider.controller;


import com.easyjobspot.backend.common.dto.ApiResponse;
import com.easyjobspot.backend.dashboard.provider.dto.ProviderDashboardStatsResponse;
import com.easyjobspot.backend.dashboard.provider.service.ProviderDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provider/dashboard")
@RequiredArgsConstructor
public class ProviderDashboardController {

    private final ProviderDashboardService dashboardService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/stats")
    public ApiResponse<ProviderDashboardStatsResponse> getDashboardStats() {
        return ApiResponse.success(
                "Provider dashboard statistics fetched successfully",
                dashboardService.getDashboardStats()
        );
    }
}
