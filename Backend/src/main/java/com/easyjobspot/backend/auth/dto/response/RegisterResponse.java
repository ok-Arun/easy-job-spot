package com.easyjobspot.backend.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterResponse {

    private String message;
    private String email;
    private String userType;
}
