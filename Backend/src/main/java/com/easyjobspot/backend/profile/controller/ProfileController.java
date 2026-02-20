package com.easyjobspot.backend.profile.controller;

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
                    profileService.userNotFoundResponse()
            );
        }

        return ResponseEntity.ok(
                profileService.getProfileStatus(user)
        );
    }

    // ================= GET MY PROFILE =================

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.ok(
                    profileService.userNotFoundResponse()
            );
        }

        return ResponseEntity.ok(
                profileService.getMyProfile(user)
        );
    }

    // ================= UPDATE MY PROFILE =================

    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(
            Authentication authentication,
            @RequestBody Object requestBody
    ) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return ResponseEntity.ok(
                    profileService.userNotFoundResponse()
            );
        }

        return ResponseEntity.ok(
                profileService.updateMyProfile(user, requestBody)
        );
    }
}
