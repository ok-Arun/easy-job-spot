package com.easyjobspot.backend.service;

import com.easyjobspot.backend.dto.request.LoginRequest;
import com.easyjobspot.backend.dto.request.RegisterRequest;
import com.easyjobspot.backend.dto.response.AuthResponse;
import com.easyjobspot.backend.entity.User;
import com.easyjobspot.backend.exception.DuplicateResourceException;
import com.easyjobspot.backend.repository.UserRepository;
import com.easyjobspot.backend.security.JwtTokenProvider;
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

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtTokenProvider.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .userType(request.getUserType())
                .build();

        userRepository.save(user);

        return new AuthResponse(
                null,
                user.getEmail(),
                user.getRole().name()
        );
    }
}
