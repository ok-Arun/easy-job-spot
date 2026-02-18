package com.easyjobspot.backend.dashboard.admin.dto;

import com.easyjobspot.backend.user.dto.UserDTO;
import lombok.*;
import org.springframework.data.domain.Page;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUsersResponse {

    private long totalUsers;
    private long seekers;
    private long providers;
    private long admins;

    private Page<UserDTO> users;
}
