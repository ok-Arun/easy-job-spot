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

        return new ProfileStatusResponse(
                user.getUserType().name(),
                user.isProfileCompleted()
        );
    }

    // ================= GET JOB SEEKER =================

    public Object getJobSeekerProfile(UUID userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return new ProfileResponse(false, "USER_NOT_FOUND");
        }

        if (!user.getUserType().name().equals("JOB_SEEKER")) {
            return new ProfileResponse(false, "PROFILE_NOT_ALLOWED_FOR_USER");
        }

        JobSeekerProfile profile =
                jobRepo.findByUserId(userId).orElse(null);

        if (profile == null) {
            return new JobSeekerProfileResponse(); // empty DTO
        }

        return new JobSeekerProfileResponse(profile);
    }

    // ================= GET PROVIDER =================

    public Object getProviderProfile(UUID userId) {

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return new ProfileResponse(false, "USER_NOT_FOUND");
        }

        if (!user.getUserType().name().equals("JOB_PROVIDER")) {
            return new ProfileResponse(false, "PROFILE_NOT_ALLOWED_FOR_USER");
        }

        ProviderProfile profile =
                providerRepo.findByUserId(userId).orElse(null);

        if (profile == null) {
            return new ProviderProfileResponse(); // empty DTO
        }

        return new ProviderProfileResponse(profile);
    }

    // ================= SAVE JOB SEEKER =================

    @Transactional
    public Object saveJobSeekerProfile(UUID userId, JobSeekerProfileRequest req) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("USER_NOT_FOUND"));

        if (!user.getUserType().name().equals("JOB_SEEKER")) {
            return new ProfileResponse(false, "PROFILE_NOT_ALLOWED_FOR_USER");
        }

        JobSeekerProfile profile =
                jobRepo.findByUserId(userId)
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

        user.setProfileCompleted(true);
        userRepository.save(user);

        return new ProfileResponse(true, "JOB_SEEKER_PROFILE_SAVED");
    }

    // ================= SAVE PROVIDER =================

    @Transactional
    public Object saveProviderProfile(UUID userId, ProviderProfileRequest req) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("USER_NOT_FOUND"));

        if (!user.getUserType().name().equals("JOB_PROVIDER")) {
            return new ProfileResponse(false, "PROFILE_NOT_ALLOWED_FOR_USER");
        }

        ProviderProfile profile =
                providerRepo.findByUserId(userId)
                        .orElseGet(() -> {
                            ProviderProfile p = new ProviderProfile();
                            p.setUserId(userId);
                            return p;
                        });

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

        user.setProfileCompleted(true);
        userRepository.save(user);

        return new ProfileResponse(true, "PROVIDER_PROFILE_SAVED");
    }
}
