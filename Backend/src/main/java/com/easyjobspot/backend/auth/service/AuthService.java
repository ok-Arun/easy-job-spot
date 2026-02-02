package com.easyjobspot.backend.auth.service;

import com.easyjobspot.backend.auth.dto.request.LoginRequest;
import com.easyjobspot.backend.auth.dto.request.RegisterRequest;
import com.easyjobspot.backend.auth.dto.response.LoginResponse;
import com.easyjobspot.backend.auth.dto.response.RegisterResponse;
import com.easyjobspot.backend.exception.BadRequestException;
import com.easyjobspot.backend.exception.DuplicateResourceException;
import com.easyjobspot.backend.security.JwtTokenProvider;
import com.easyjobspot.backend.user.entity.User;
import com.easyjobspot.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    // =====================================================
    // REGISTRATION
    // =====================================================
    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        User.ProviderStatus providerStatus = null;

        if (request.getUserType() == User.UserType.JOB_PROVIDER) {
            providerStatus = User.ProviderStatus.PENDING;
        }

        boolean profileCompleted =
                request.getUserType() == User.UserType.JOB_SEEKER
                        || request.getUserType() == User.UserType.JOB_PROVIDER;

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .userType(request.getUserType())
                .providerStatus(providerStatus)
                .profileCompleted(false) // seekers & providers start incomplete
                .build();

        userRepository.save(user);

        return new RegisterResponse(
                "Registration successful.",
                user.getEmail(),
                user.getUserType().name()
        );
    }

    // =====================================================
    // LOGIN
    // =====================================================
    public LoginResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (Exception ex) {
            throw new BadRequestException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new BadRequestException("Invalid email or password")
                );

        if (user.getUserType() == User.UserType.JOB_PROVIDER) {

            if (user.getProviderStatus() == User.ProviderStatus.PENDING) {
                throw new BadRequestException(
                        "Your account is pending admin approval."
                );
            }

            if (user.getProviderStatus() == User.ProviderStatus.REJECTED) {
                throw new BadRequestException(
                        "Your account has been rejected by admin."
                );
            }
        }

        String token = jwtTokenProvider.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        String message =
                "Welcome " + user.getName() + ", login successful.";

        return new LoginResponse(
                message,
                token,
                "Bearer",
                user.getEmail(),
                user.getRole().name(),
                user.getUserType().name()
        );
    }
}
