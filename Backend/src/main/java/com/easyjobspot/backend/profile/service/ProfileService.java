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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final JobSeekerProfileRepository jobRepo;
    private final ProviderProfileRepository providerRepo;
    private final ProviderApprovalRepository providerApprovalRepository;
    private final ObjectMapper objectMapper;

    public ProfileService(
            UserRepository userRepository,
            JobSeekerProfileRepository jobRepo,
            ProviderProfileRepository providerRepo,
            ProviderApprovalRepository providerApprovalRepository,
            ObjectMapper objectMapper
    ) {
        this.userRepository = userRepository;
        this.jobRepo = jobRepo;
        this.providerRepo = providerRepo;
        this.providerApprovalRepository = providerApprovalRepository;
        this.objectMapper = objectMapper;
    }

    // ================= COMMON =================

    public ProfileResponse userNotFoundResponse() {
        return new ProfileResponse(false, "USER_NOT_FOUND");
    }

    public ProfileStatusResponse getProfileStatus(User user) {
        return new ProfileStatusResponse(
                user.getUserType().name(),
                user.isProfileCompleted()
        );
    }

    // ================= GET PROFILE =================

    public Object getMyProfile(User user) {

        switch (user.getUserType()) {

            case JOB_SEEKER -> {
                JobSeekerProfile profile =
                        jobRepo.findByUserId(user.getId()).orElse(null);

                if (profile == null) {
                    return new JobSeekerProfileResponse();
                }

                return new JobSeekerProfileResponse(profile);
            }

            case JOB_PROVIDER -> {
                ProviderProfile profile =
                        providerRepo.findByUserId(user.getId()).orElse(null);

                if (profile == null) {
                    return new ProviderProfileResponse();
                }

                return new ProviderProfileResponse(profile);
            }

            case SYSTEM_ADMIN -> {
                return new AdminProfileResponse(
                        user.getEmail(),
                        user.getUserType().name(),
                        user.isProfileCompleted()
                );
            }

            default -> throw new BadRequestException("INVALID_USER_TYPE");
        }
    }

    // ================= UPDATE PROFILE =================

    @Transactional
    public Object updateMyProfile(User user, Object requestBody) {

        switch (user.getUserType()) {

            case JOB_SEEKER -> {
                JobSeekerProfileRequest req =
                        objectMapper.convertValue(requestBody, JobSeekerProfileRequest.class);

                return saveJobSeeker(user, req);
            }

            case JOB_PROVIDER -> {
                ProviderProfileRequest req =
                        objectMapper.convertValue(requestBody, ProviderProfileRequest.class);

                return saveProvider(user, req);
            }

            case SYSTEM_ADMIN -> {
                return new ProfileResponse(false, "ADMIN_PROFILE_EDIT_NOT_AVAILABLE");
            }

            default -> throw new BadRequestException("INVALID_USER_TYPE");
        }
    }

    // ================= PRIVATE SAVE METHODS =================

    private ProfileResponse saveJobSeeker(User user, JobSeekerProfileRequest req) {

        JobSeekerProfile profile =
                jobRepo.findByUserId(user.getId())
                        .orElseGet(() -> {
                            JobSeekerProfile p = new JobSeekerProfile();
                            p.setUserId(user.getId());
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

    private ProfileResponse saveProvider(User user, ProviderProfileRequest req) {

        ProviderProfile profile =
                providerRepo.findByUserId(user.getId())
                        .orElseGet(() -> {
                            ProviderProfile p = new ProviderProfile();
                            p.setUserId(user.getId());
                            return p;
                        });

        if (profile.getApprovedAt() == null) {
            ProviderApproval approval =
                    providerApprovalRepository.findByProviderId(user.getId())
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
