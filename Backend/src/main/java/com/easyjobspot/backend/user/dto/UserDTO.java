package com.easyjobspot.backend.user.dto;


import com.easyjobspot.backend.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UserDTO {

    private UUID id;
    private String name;
    private String email;
    private User.Role role;
    private String userType;
    private LocalDateTime createdAt;
}
