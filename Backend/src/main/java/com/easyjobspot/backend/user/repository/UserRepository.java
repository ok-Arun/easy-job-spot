package com.easyjobspot.backend.user.repository;

import com.easyjobspot.backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByUserTypeAndProviderStatus(
            User.UserType userType,
            User.ProviderStatus providerStatus
    );

    // ===== Dashboard Counts =====

    long countByUserType(User.UserType userType);

    long countByUserTypeAndProviderStatus(
            User.UserType userType,
            User.ProviderStatus providerStatus
    );

    long countByRole(User.Role role);

    // ===== Admin Users Pagination & Search =====

    Page<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String name,
            String email,
            Pageable pageable
    );

    // 🔹 NEW — filter by userType
    Page<User> findByUserType(User.UserType userType, Pageable pageable);

    // 🔹 NEW — search + userType together
    Page<User> findByUserTypeAndNameContainingIgnoreCaseOrUserTypeAndEmailContainingIgnoreCase(
            User.UserType userType1, String name,
            User.UserType userType2, String email,
            Pageable pageable
    );
}
