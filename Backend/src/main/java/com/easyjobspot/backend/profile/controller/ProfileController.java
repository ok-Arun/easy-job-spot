package com.easyjobspot.backend.profile.controller;

import com.easyjobspot.backend.profile.dto.*;
import com.easyjobspot.backend.profile.service.ProfileService;
import com.easyjobspot.backend.user.entity.User;
import com.easyjobspot.backend.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final UserRepository userRepository;

    public ProfileController(
            ProfileService profileService,
            UserRepository userRepository
    ) {
        this.profileService = profileService;
        this.userRepository = userRepository;
    }

    // ================= STATUS =================

    @GetMapping("/status")
    public ResponseEntity<?> getStatus(Authentication authentication) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.ok(
                    new ProfileResponse(false, "USER_NOT_FOUND")
            );
        }

        return ResponseEntity.ok(
                profileService.getProfileStatus(user.getId())
        );
    }

    // ================= JOB SEEKER =================

    @PutMapping("/job-seeker")
    public ResponseEntity<?> updateJobSeeker(
            Authentication authentication,
            @RequestBody JobSeekerProfileRequest request
    ) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.ok(
                    new ProfileResponse(false, "USER_NOT_FOUND")
            );
        }

        Object response =
                profileService.saveJobSeekerProfile(user.getId(), request);

        return ResponseEntity.ok(response);
    }

    // ================= PROVIDER =================

    @PutMapping("/provider")
    public ResponseEntity<?> updateProvider(
            Authentication authentication,
            @RequestBody ProviderProfileRequest request
    ) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.ok(
                    new ProfileResponse(false, "USER_NOT_FOUND")
            );
        }

        Object response =
                profileService.saveProviderProfile(user.getId(), request);

        return ResponseEntity.ok(response);
    }
}
