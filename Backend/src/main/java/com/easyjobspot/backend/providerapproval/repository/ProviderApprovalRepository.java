package com.easyjobspot.backend.providerapproval.repository;

import com.easyjobspot.backend.providerapproval.entity.ProviderApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProviderApprovalRepository
        extends JpaRepository<ProviderApproval, UUID> {

    // fetch approval record using provider(user) id
    Optional<ProviderApproval> findByProviderId(UUID providerId);
}
