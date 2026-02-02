package com.easyjobspot.backend.profile.dto;

import java.util.List;

public class ProfileValidationResponse implements ProfileApiResponse {

    private String error;
    private List<String> missingFields;

    public ProfileValidationResponse(String error, List<String> missingFields) {
        this.error = error;
        this.missingFields = missingFields;
    }

    public String getError() {
        return error;
    }

    public List<String> getMissingFields() {
        return missingFields;
    }
}
