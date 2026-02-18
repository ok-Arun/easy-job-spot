package com.easyjobspot.backend.dashboard.admin.service;

import com.easyjobspot.backend.dashboard.admin.dto.AdminUsersResponse;
import com.easyjobspot.backend.user.dto.UserDTO;
import com.easyjobspot.backend.user.entity.User;
import com.easyjobspot.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUsersService {

    private final UserRepository userRepository;

    public AdminUsersResponse getAllUsers(String search,
                                          String userType,
                                          int page,
                                          int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        Page<User> usersPage;

        boolean hasSearch = search != null && !search.isBlank();
        boolean hasUserType = userType != null && !userType.isBlank();

        // ===== filtering by search + userType =====
        if (hasSearch && hasUserType) {
            usersPage = userRepository
                    .findByUserTypeAndNameContainingIgnoreCaseOrUserTypeAndEmailContainingIgnoreCase(
                            User.UserType.valueOf(userType), search,
                            User.UserType.valueOf(userType), search,
                            pageable
                    );

        } else if (hasSearch) {
            usersPage = userRepository
                    .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            search, search, pageable
                    );

        } else if (hasUserType) {
            usersPage = userRepository
                    .findByUserType(User.UserType.valueOf(userType), pageable);

        } else {
            usersPage = userRepository.findAll(pageable);
        }

        // ===== map to DTO =====
        Page<UserDTO> dtoPage = usersPage.map(user ->
                UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .userType(user.getUserType().name())
                        .createdAt(user.getCreatedAt())
                        .build()
        );

        // ===== stats =====
        long totalUsers = userRepository.count();
        long seekers = userRepository.countByUserType(User.UserType.JOB_SEEKER);
        long providers = userRepository.countByUserType(User.UserType.JOB_PROVIDER);
        long admins = userRepository.countByUserType(User.UserType.SYSTEM_ADMIN);

        return AdminUsersResponse.builder()
                .totalUsers(totalUsers)
                .seekers(seekers)
                .providers(providers)
                .admins(admins)
                .users(dtoPage)
                .build();
    }
}
