package com.easyjobspot.backend.profile.dto;

public class ProfileStatusResponse implements ProfileApiResponse {

    private String userType;
    private boolean profileCompleted;

    public ProfileStatusResponse(String userType, boolean profileCompleted) {
        this.userType = userType;
        this.profileCompleted = profileCompleted;
    }

    public String getUserType() {
        return userType;
    }

    public boolean isProfileCompleted() {
        return profileCompleted;
    }
}
