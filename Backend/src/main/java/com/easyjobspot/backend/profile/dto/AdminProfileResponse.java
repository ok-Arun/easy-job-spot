package com.easyjobspot.backend.profile.dto;

public class AdminProfileResponse {

    private String email;
    private String userType;
    private boolean profileCompleted;

    public AdminProfileResponse(String email, String userType, boolean profileCompleted) {
        this.email = email;
        this.userType = userType;
        this.profileCompleted = profileCompleted;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }
}
