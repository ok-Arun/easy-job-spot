package com.easyjobspot.backend.providerapproval.controller;

import com.easyjobspot.backend.providerapproval.dto.PendingProviderListResponse;
import com.easyjobspot.backend.providerapproval.dto.ProviderDecisionResponse;
import com.easyjobspot.backend.providerapproval.service.ProviderApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/providers")
@RequiredArgsConstructor
public class ProviderApprovalController {

    private final ProviderApprovalService providerApprovalService;

    // =================================================
    // FETCH PENDING PROVIDERS
    // =================================================
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PendingProviderListResponse> getPendingProviders() {
        return ResponseEntity.ok(
                providerApprovalService.getPendingProviders()
        );
    }

    // =================================================
    // APPROVE PROVIDER
    // =================================================
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProviderDecisionResponse> approveProvider(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                providerApprovalService.approveProvider(id)
        );
    }

    // =================================================
    // REJECT PROVIDER
    // =================================================
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProviderDecisionResponse> rejectProvider(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                providerApprovalService.rejectProvider(id)
        );
    }
}
