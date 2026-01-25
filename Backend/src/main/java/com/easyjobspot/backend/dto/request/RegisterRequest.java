package com.easyjobspot.backend.dto.request;

import com.easyjobspot.backend.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    // JOB_SEEKER or JOB_PROVIDER
    private User.UserType userType;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public User.UserType getUserType() {
        return userType;
    }
}
