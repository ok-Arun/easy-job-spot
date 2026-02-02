package com.easyjobspot.backend.providerapproval.service;

import com.easyjobspot.backend.exception.BadRequestException;
import com.easyjobspot.backend.providerapproval.dto.PendingProviderListResponse;
import com.easyjobspot.backend.providerapproval.dto.PendingProviderResponse;
import com.easyjobspot.backend.providerapproval.dto.ProviderDecisionResponse;
import com.easyjobspot.backend.providerapproval.entity.ProviderApproval;
import com.easyjobspot.backend.providerapproval.repository.ProviderApprovalRepository;
import com.easyjobspot.backend.user.entity.User;
import com.easyjobspot.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProviderApprovalService {

    private final UserRepository userRepository;
    private final ProviderApprovalRepository providerApprovalRepository;

    // =================================================
    // FETCH PENDING PROVIDERS
    // =================================================
    public PendingProviderListResponse getPendingProviders() {

        List<User> providers =
                userRepository.findByUserTypeAndProviderStatus(
                        User.UserType.JOB_PROVIDER,
                        User.ProviderStatus.PENDING
                );

        List<PendingProviderResponse> response =
                providers.stream()
                        .map(user -> new PendingProviderResponse(
                                user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getProviderStatus().name()
                        ))
                        .toList();

        return new PendingProviderListResponse(
                response.size(),
                response
        );
    }

    // =================================================
    // APPROVE PROVIDER
    // =================================================
    @Transactional
    public ProviderDecisionResponse approveProvider(UUID providerId) {

        User provider = getPendingProvider(providerId);

        // prevent duplicate approval record
        providerApprovalRepository.findByProviderId(providerId)
                .ifPresent(a -> {
                    throw new BadRequestException("Provider already approved");
                });

        // update user table
        provider.setProviderStatus(User.ProviderStatus.APPROVED);
        userRepository.save(provider);

        // approval metadata
        UUID adminId = getCurrentAdminId();

        ProviderApproval approval = new ProviderApproval(
                providerId,
                adminId,
                LocalDateTime.now()
        );

        providerApprovalRepository.save(approval);

        return new ProviderDecisionResponse(
                "Provider approved successfully",
                provider.getEmail(),
                provider.getProviderStatus().name()
        );
    }

    // =================================================
    // REJECT PROVIDER
    // =================================================
    @Transactional
    public ProviderDecisionResponse rejectProvider(UUID providerId) {

        User provider = getPendingProvider(providerId);

        provider.setProviderStatus(User.ProviderStatus.REJECTED);
        userRepository.save(provider);

        return new ProviderDecisionResponse(
                "Provider rejected successfully",
                provider.getEmail(),
                provider.getProviderStatus().name()
        );
    }

    // =================================================
    // VALIDATION
    // =================================================
    private User getPendingProvider(UUID providerId) {

        User user = userRepository.findById(providerId)
                .orElseThrow(() ->
                        new BadRequestException("Provider not found")
                );

        if (user.getUserType() != User.UserType.JOB_PROVIDER) {
            throw new BadRequestException("User is not a job provider");
        }

        if (user.getProviderStatus() != User.ProviderStatus.PENDING) {
            throw new BadRequestException(
                    "Provider already " + user.getProviderStatus()
            );
        }

        return user;
    }

    // =================================================
    // ADMIN FROM SECURITY CONTEXT
    // =================================================
    private UUID getCurrentAdminId() {

        String adminEmail =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() ->
                        new BadRequestException("Admin not found")
                );

        return admin.getId();
    }
}
