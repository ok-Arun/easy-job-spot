package com.easyjobspot.backend.providerapproval.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "provider_approval",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "provider_id")
        }
)
public class ProviderApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * This is the JOB_PROVIDER user id
     * One approval record per provider
     */
    @Column(name = "provider_id", nullable = false, updatable = false)
    private UUID providerId;

    /**
     * Admin user id who approved
     */
    @Column(name = "approved_by", nullable = false, updatable = false)
    private UUID approvedBy;

    /**
     * Approval timestamp
     */
    @Column(name = "approved_at", nullable = false, updatable = false)
    private LocalDateTime approvedAt;

    // ================= CONSTRUCTORS =================

    protected ProviderApproval() {
        // JPA only
    }

    public ProviderApproval(
            UUID providerId,
            UUID approvedBy,
            LocalDateTime approvedAt
    ) {
        this.providerId = providerId;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
    }

    // ================= GETTERS =================

    public UUID getId() {
        return id;
    }

    public UUID getProviderId() {
        return providerId;
    }

    public UUID getApprovedBy() {
        return approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }
}
