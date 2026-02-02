package com.easyjobspot.backend.user.repository;

import com.easyjobspot.backend.user.entity.User;
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
}
