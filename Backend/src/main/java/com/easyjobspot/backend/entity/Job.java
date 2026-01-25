package com.easyjobspot.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 200)
    private String company;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false, length = 200)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private JobType jobType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Job application deadline
     * Used by scheduler to expire job automatically
     */
    private LocalDateTime deadline;

    /**
     * UUID of job creator (JOB_PROVIDER or ADMIN)
     */
    @Column(nullable = false, updatable = false)
    private UUID createdBy;

    /**
     * Job moderation status
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private JobStatus status;

    /**
     * Admin rejection reason (only when REJECTED)
     */
    @Column(length = 1000)
    private String rejectionReason;

    // ====================================================
    // LIFECYCLE
    // ====================================================
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = JobStatus.PENDING_APPROVAL;
    }

    // ====================================================
    // SAFE UPDATE (NO STATUS CHANGE)
    // ====================================================
    public void update(
            String title,
            String company,
            String category,
            String location,
            JobType jobType,
            String description
    ) {
        this.title = title;
        this.company = company;
        this.category = category;
        this.location = location;
        this.jobType = jobType;
        this.description = description;
    }

    // ====================================================
    // DOMAIN STATE TRANSITIONS
    // ====================================================

    public void approve() {
        if (this.status != JobStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Only pending jobs can be approved");
        }
        this.status = JobStatus.ACTIVE;
        this.rejectionReason = null;
    }

    public void reject(String reason) {
        if (this.status != JobStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Only pending jobs can be rejected");
        }
        this.status = JobStatus.REJECTED;
        this.rejectionReason = reason;
    }

    public void close() {
        if (this.status != JobStatus.ACTIVE) {
            throw new IllegalStateException("Only active jobs can be closed");
        }
        this.status = JobStatus.CLOSED;
    }

    public void expire() {
        if (this.status != JobStatus.ACTIVE) {
            throw new IllegalStateException("Only active jobs can be expired");
        }
        this.status = JobStatus.EXPIRED;
    }

    // ====================================================
    // ENUMS
    // ====================================================
    public enum JobType {
        FULL_TIME,
        PART_TIME,
        INTERNSHIP,
        CONTRACT
    }

    public enum JobStatus {
        PENDING_APPROVAL,
        ACTIVE,
        REJECTED,
        CLOSED,
        EXPIRED
    }
}
