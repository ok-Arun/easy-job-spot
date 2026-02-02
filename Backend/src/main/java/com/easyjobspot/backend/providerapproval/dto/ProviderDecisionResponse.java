package com.easyjobspot.backend.providerapproval.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProviderDecisionResponse {

    private String message;
    private String email;
    private String providerStatus;
}
