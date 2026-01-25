package com.easyjobspot.backend.entity;

import com.easyjobspot.backend.enums.AuditAction;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admin_audit_logs")
public class AdminAuditLog {

    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Admin who performed the action
     */
    @Column(nullable = false)
    private UUID adminId;

    /**
     * What action was performed
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AuditAction action;

    /**
     * Target entity ID (jobId / applicationId)
     */
    @Column(nullable = false)
    private UUID targetId;

    /**
     * Optional note or reason
     */
    @Column(length = 1000)
    private String note;

    /**
     * Timestamp
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ====================================================
    // LIFECYCLE
    // ====================================================
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // ====================================================
    // GETTERS
    // ====================================================
    public UUID getId() {
        return id;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public AuditAction getAction() {
        return action;
    }

    public UUID getTargetId() {
        return targetId;
    }

    public String getNote() {
        return note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ====================================================
    // SETTERS
    // ====================================================
    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public void setAction(AuditAction action) {
        this.action = action;
    }

    public void setTargetId(UUID targetId) {
        this.targetId = targetId;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
