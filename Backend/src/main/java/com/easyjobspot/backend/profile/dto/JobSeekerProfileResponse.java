package com.easyjobspot.backend.profile.dto;

import com.easyjobspot.backend.profile.entity.JobSeekerProfile;

public class JobSeekerProfileResponse {

    private String firstName;
    private String lastName;
    private String phone;
    private String location;
    private String skills;
    private String experience;
    private String education;
    private String resumeUrl;
    private String currentJobTitle;
    private String preferredJobType;
    private String preferredLocation;
    private String noticePeriod;
    private String linkedinUrl;
    private String portfolioUrl;
    private boolean profileCompleted;

    public JobSeekerProfileResponse() {
        // empty response (used when profile not created yet)
    }

    public JobSeekerProfileResponse(JobSeekerProfile profile) {
        this.firstName = profile.getFirstName();
        this.lastName = profile.getLastName();
        this.phone = profile.getPhone();
        this.location = profile.getLocation();
        this.skills = profile.getSkills();
        this.experience = profile.getExperience();
        this.education = profile.getEducation();
        this.resumeUrl = profile.getResumeUrl();
        this.currentJobTitle = profile.getCurrentJobTitle();
        this.preferredJobType = profile.getPreferredJobType();
        this.preferredLocation = profile.getPreferredLocation();
        this.noticePeriod = profile.getNoticePeriod();
        this.linkedinUrl = profile.getLinkedinUrl();
        this.portfolioUrl = profile.getPortfolioUrl();
        this.profileCompleted = profile.isProfileCompleted();
    }

    // Getters

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public String getLocation() { return location; }
    public String getSkills() { return skills; }
    public String getExperience() { return experience; }
    public String getEducation() { return education; }
    public String getResumeUrl() { return resumeUrl; }
    public String getCurrentJobTitle() { return currentJobTitle; }
    public String getPreferredJobType() { return preferredJobType; }
    public String getPreferredLocation() { return preferredLocation; }
    public String getNoticePeriod() { return noticePeriod; }
    public String getLinkedinUrl() { return linkedinUrl; }
    public String getPortfolioUrl() { return portfolioUrl; }
    public boolean isProfileCompleted() { return profileCompleted; }
}
