package com.easyjobspot.backend.profile.dto;

public class ProfileResponse implements ProfileApiResponse {

    private boolean success;
    private String message;

    public ProfileResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
