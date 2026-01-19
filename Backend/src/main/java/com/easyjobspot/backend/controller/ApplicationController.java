package com.easyjobspot.backend.controller;

import com.easyjobspot.backend.dto.ApplicationDTO;
import com.easyjobspot.backend.security.UserPrincipal;
import com.easyjobspot.backend.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@Slf4j
public class ApplicationController {

    private final ApplicationService applicationService;

    @PostMapping("/{jobId}")
    public ResponseEntity<ApplicationDTO> applyForJob(
            @PathVariable UUID jobId,
            @AuthenticationPrincipal UserPrincipal currentUser) {
        log.info("POST /api/applications/{} - User {} applying", jobId, currentUser.getId());
        ApplicationDTO application = applicationService.applyForJob(jobId, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ApplicationDTO>> getMyApplications(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        log.info("GET /api/applications/my - User: {}", currentUser.getId());
        List<ApplicationDTO> applications = applicationService.getMyApplications(currentUser);
        return ResponseEntity.ok(applications);
    }
}