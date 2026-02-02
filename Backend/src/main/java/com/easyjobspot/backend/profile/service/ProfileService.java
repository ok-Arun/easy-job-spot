package com.easyjobspot.backend.profile.service;

import com.easyjobspot.backend.exception.BadRequestException;
import com.easyjobspot.backend.profile.dto.*;
import com.easyjobspot.backend.profile.entity.JobSeekerProfile;
import com.easyjobspot.backend.profile.entity.ProviderProfile;
import com.easyjobspot.backend.profile.repository.JobSeekerProfileRepository;
import com.easyjobspot.backend.profile.repository.ProviderProfileRepository;
import com.easyjobspot.backend.providerapproval.entity.ProviderApproval;
import com.easyjobspot.backend.providerapproval.repository.ProviderApprovalRepository;
import com.easyjobspot.backend.user.entity.User;
import com.easyjobspot.backend.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final JobSeekerProfileRepository jobRepo;
    private final ProviderProfileRepository providerRepo;
    private final ProviderApprovalRepository providerApprovalRepository;

    public ProfileService(
            UserRepository userRepository,
            JobSeekerProfileRepository jobRepo,
            ProviderProfileRepository providerRepo,
            ProviderApprovalRepository providerApprovalRepository
    ) {
        this.userRepository = userRepository;
        this.jobRepo = jobRepo;
        this.providerRepo = providerRepo;
        this.providerApprovalRepository = providerApprovalRepository;
    }

    // ================= STATUS =================

    public ProfileStatusResponse getProfileStatus(UUID userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return new ProfileStatusResponse("UNKNOWN", false);
        }

        String userType = user.getUserType().name();

        if (userType.equals("SYSTEM_ADMIN")) {
            return new ProfileStatusResponse(userType, true);
        }

        if (userType.equals("JOB_SEEKER")) {
            boolean completed = jobRepo.findByUserId(userId)
                    .map(JobSeekerProfile::isProfileCompleted)
                    .orElse(false);

            return new ProfileStatusResponse(userType, completed);
        }

        if (userType.equals("JOB_PROVIDER")) {
            boolean completed = providerRepo.findByUserId(userId)
                    .map(ProviderProfile::isProfileCompleted)
                    .orElse(false);

            return new ProfileStatusResponse(userType, completed);
        }

        return new ProfileStatusResponse(userType, false);
    }

    // ================= JOB SEEKER =================

    @Transactional
    public Object saveJobSeekerProfile(UUID userId, JobSeekerProfileRequest req) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return new ProfileResponse(false, "USER_NOT_FOUND");
        }

        if (!user.getUserType().name().equals("JOB_SEEKER")) {
            return new ProfileResponse(false, "PROFILE_NOT_ALLOWED_FOR_USER");
        }

        List<String> missingFields = new ArrayList<>();

        if (isBlank(req.getFirstName())) missingFields.add("firstName");
        if (isBlank(req.getLastName())) missingFields.add("lastName");
        if (isBlank(req.getPhone())) missingFields.add("phone");
        if (isBlank(req.getLocation())) missingFields.add("location");
        if (isBlank(req.getSkills())) missingFields.add("skills");
        if (isBlank(req.getExperience())) missingFields.add("experience");
        if (isBlank(req.getEducation())) missingFields.add("education");
        if (isBlank(req.getResumeUrl())) missingFields.add("resumeUrl");
        if (isBlank(req.getCurrentJobTitle())) missingFields.add("currentJobTitle");
        if (isBlank(req.getPreferredJobType())) missingFields.add("preferredJobType");
        if (isBlank(req.getPreferredLocation())) missingFields.add("preferredLocation");
        if (isBlank(req.getNoticePeriod())) missingFields.add("noticePeriod");
        if (isBlank(req.getLinkedinUrl())) missingFields.add("linkedinUrl");
        if (isBlank(req.getPortfolioUrl())) missingFields.add("portfolioUrl");

        if (!missingFields.isEmpty()) {
            return new ProfileValidationResponse(
                    "REQUIRED_FIELDS_MISSING",
                    missingFields
            );
        }

        JobSeekerProfile profile = jobRepo.findByUserId(userId)
                .orElseGet(() -> {
                    JobSeekerProfile p = new JobSeekerProfile();
                    p.setUserId(userId);
                    return p;
                });

        profile.setFirstName(req.getFirstName());
        profile.setLastName(req.getLastName());
        profile.setPhone(req.getPhone());
        profile.setLocation(req.getLocation());
        profile.setSkills(req.getSkills());
        profile.setExperience(req.getExperience());
        profile.setEducation(req.getEducation());
        profile.setResumeUrl(req.getResumeUrl());

        profile.setCurrentJobTitle(req.getCurrentJobTitle());
        profile.setPreferredJobType(req.getPreferredJobType());
        profile.setPreferredLocation(req.getPreferredLocation());
        profile.setNoticePeriod(req.getNoticePeriod());
        profile.setLinkedinUrl(req.getLinkedinUrl());
        profile.setPortfolioUrl(req.getPortfolioUrl());

        profile.setProfileCompleted(true);

        jobRepo.save(profile);

        return new ProfileResponse(true, "JOB_SEEKER_PROFILE_SAVED");
    }

    // ================= PROVIDER =================

    @Transactional
    public Object saveProviderProfile(UUID userId, ProviderProfileRequest req) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return new ProfileResponse(false, "USER_NOT_FOUND");
        }

        if (!user.getUserType().name().equals("JOB_PROVIDER")) {
            return new ProfileResponse(false, "PROFILE_NOT_ALLOWED_FOR_USER");
        }

        List<String> missingFields = new ArrayList<>();

        if (isBlank(req.getCompanyName())) missingFields.add("companyName");
        if (isBlank(req.getCompanyEmail())) missingFields.add("companyEmail");
        if (isBlank(req.getCompanyPhone())) missingFields.add("companyPhone");
        if (isBlank(req.getWebsite())) missingFields.add("website");
        if (isBlank(req.getAddress())) missingFields.add("address");
        if (isBlank(req.getDescription())) missingFields.add("description");

        if (!missingFields.isEmpty()) {
            return new ProfileValidationResponse(
                    "REQUIRED_FIELDS_MISSING",
                    missingFields
            );
        }

        ProviderProfile profile = providerRepo.findByUserId(userId)
                .orElseGet(() -> {
                    ProviderProfile p = new ProviderProfile();
                    p.setUserId(userId);
                    return p;
                });

        // ✅ correct approval fetch
        if (profile.getApprovedAt() == null) {

            ProviderApproval approval =
                    providerApprovalRepository.findByProviderId(userId)
                            .orElseThrow(() ->
                                    new BadRequestException("Provider not approved yet")
                            );

            profile.setApprovedBy(approval.getApprovedBy());
            profile.setApprovedAt(approval.getApprovedAt());
        }

        profile.setCompanyName(req.getCompanyName());
        profile.setCompanyEmail(req.getCompanyEmail());
        profile.setCompanyPhone(req.getCompanyPhone());
        profile.setWebsite(req.getWebsite());
        profile.setAddress(req.getAddress());
        profile.setDescription(req.getDescription());
        profile.setProfileCompleted(true);

        providerRepo.save(profile);

        return new ProfileResponse(true, "PROVIDER_PROFILE_SAVED");
    }

    // ================= UTILITY =================

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
