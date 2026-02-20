package com.easyjobspot.backend.profile.dto;

import com.easyjobspot.backend.profile.entity.ProviderProfile;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProviderProfileResponse {

    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String website;
    private String address;
    private String description;
    private UUID approvedBy;
    private LocalDateTime approvedAt;
    private boolean profileCompleted;

    public ProviderProfileResponse() {
        // empty response if profile not yet created
    }

    public ProviderProfileResponse(ProviderProfile profile) {
        this.companyName = profile.getCompanyName();
        this.companyEmail = profile.getCompanyEmail();
        this.companyPhone = profile.getCompanyPhone();
        this.website = profile.getWebsite();
        this.address = profile.getAddress();
        this.description = profile.getDescription();
        this.approvedBy = profile.getApprovedBy();
        this.approvedAt = profile.getApprovedAt();
        this.profileCompleted = profile.isProfileCompleted();
    }

    // Getters

    public String getCompanyName() { return companyName; }
    public String getCompanyEmail() { return companyEmail; }
    public String getCompanyPhone() { return companyPhone; }
    public String getWebsite() { return website; }
    public String getAddress() { return address; }
    public String getDescription() { return description; }
    public UUID getApprovedBy() { return approvedBy; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public boolean isProfileCompleted() { return profileCompleted; }
}
