package com.easyjobspot.backend.service;

import com.easyjobspot.backend.dto.AuthResponse;
import com.easyjobspot.backend.dto.LoginRequest;
import com.easyjobspot.backend.dto.RegisterRequest;
import com.easyjobspot.backend.dto.UserDTO;
import com.easyjobspot.backend.entity.User;
import com.easyjobspot.backend.exception.DuplicateResourceException;
import com.easyjobspot.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * REGISTER
     * - Save user
     * - Return JSON
     * - NO authentication here
     */
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");

        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.USER);// ✅ correct enum
        user.setUserType(request.getUserType());

        userRepository.save(user);

        UserDTO userDTO = mapToUserDTO(user);

        return new AuthResponse(
                null,               // token handled on login
                "REGISTERED",
                userDTO
        );
    }

    /**
     * LOGIN
     */
    public AuthResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDTO userDTO = mapToUserDTO(user);

        return new AuthResponse(
                null,
                "LOGGED_IN",
                userDTO
        );
    }

    /**
     * Map User -> UserDTO (minimal, safe)
     */
    private UserDTO mapToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .userType(user.getUserType())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
