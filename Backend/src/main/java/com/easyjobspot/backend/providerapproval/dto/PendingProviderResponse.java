package com.easyjobspot.backend.providerapproval.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class PendingProviderResponse {

    private UUID id;
    private String name;
    private String email;
    private String providerStatus;
}
