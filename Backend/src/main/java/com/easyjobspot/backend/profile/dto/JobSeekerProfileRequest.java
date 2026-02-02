package com.easyjobspot.backend.profile.dto;

public class JobSeekerProfileRequest {

    private String firstName;
    private String lastName;
    private String phone;
    private String location;
    private String skills;
    private String experience;
    private String education;
    private String resumeUrl;

    // ===== NEW REQUIRED FIELDS =====

    private String currentJobTitle;
    private String preferredJobType;
    private String preferredLocation;
    private String noticePeriod;
    private String linkedinUrl;
    private String portfolioUrl;

    // ===== GETTERS =====

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public String getSkills() {
        return skills;
    }

    public String getExperience() {
        return experience;
    }

    public String getEducation() {
        return education;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public String getCurrentJobTitle() {
        return currentJobTitle;
    }

    public String getPreferredJobType() {
        return preferredJobType;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public String getNoticePeriod() {
        return noticePeriod;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }
}
