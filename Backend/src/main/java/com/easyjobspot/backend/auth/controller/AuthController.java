package com.easyjobspot.backend.auth.controller;

import com.easyjobspot.backend.auth.dto.request.LoginRequest;
import com.easyjobspot.backend.auth.dto.request.RegisterRequest;
import com.easyjobspot.backend.auth.dto.response.LoginResponse;
import com.easyjobspot.backend.auth.dto.response.RegisterResponse;
import com.easyjobspot.backend.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    // ================= REGISTER =================

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        log.info("POST /api/auth/register - Register user: {}", request.getEmail());
        return ResponseEntity.ok(authService.register(request));
    }

    // ================= LOGIN =================

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        log.info("POST /api/auth/login - User login: {}", request.getEmail());
        return ResponseEntity.ok(authService.login(request));
    }
}
