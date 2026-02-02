package com.easyjobspot.backend.providerapproval.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PendingProviderListResponse {

    private long totalPending;
    private List<PendingProviderResponse> providers;
}
