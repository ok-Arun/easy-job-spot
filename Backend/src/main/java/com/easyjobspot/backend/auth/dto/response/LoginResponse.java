package com.easyjobspot.backend.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String message;
    private String token;
    private String type;
    private String name;
    private String email;
    private String role;
    private String userType;
}
