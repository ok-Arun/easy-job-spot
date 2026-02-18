package com.easyjobspot.backend.dashboard.admin.controller;

import com.easyjobspot.backend.common.dto.ApiResponse;
import com.easyjobspot.backend.dashboard.admin.dto.AdminUsersResponse;
import com.easyjobspot.backend.dashboard.admin.service.AdminUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUsersController {

    private final AdminUsersService adminUsersService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String userType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        AdminUsersResponse response =
                adminUsersService.getAllUsers(search, userType, page, size);

        return ResponseEntity.ok(
                new ApiResponse(true, "Users fetched successfully", response)
        );
    }

}
