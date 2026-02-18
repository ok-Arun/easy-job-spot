package com.easyjobspot.backend.job.entity;

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

    // ================= EXTRA FIELDS =================

    @Column(length = 30)
    private String workMode;

    @Column(length = 30)
    private String employmentLevel;

    private Double salaryMin;
    private Double salaryMax;

    private Integer experienceMin;
    private Integer experienceMax;

    private Integer vacancyCount;

    @Column(length = 30)
    private String applicationType;

    @Column(length = 500)
    private String applicationUrl;

    // ===============================================

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deadline;

    @Column(nullable = false, updatable = false)
    private UUID createdBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private JobStatus status;

    @Column(length = 1000)
    private String rejectionReason;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = JobStatus.PENDING_APPROVAL;
    }

    // ====================================================
    // SAFE UPDATE
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
    // DOMAIN TRANSITIONS
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

    public void activate() {
        if (this.status != JobStatus.CLOSED) {
            throw new IllegalStateException("Only closed jobs can be activated");
        }
        this.status = JobStatus.ACTIVE;
    }

    // ====================================================
    // ADMIN ENFORCEMENT TRANSITIONS
    // ====================================================

    public void removeByAdmin() {
        if (this.status == JobStatus.REMOVED_BY_ADMIN) {
            throw new IllegalStateException("Job already removed by admin");
        }
        this.status = JobStatus.REMOVED_BY_ADMIN;
    }

    public void restoreByAdmin() {
        if (this.status != JobStatus.REMOVED_BY_ADMIN) {
            throw new IllegalStateException("Only removed jobs can be restored");
        }
        this.status = JobStatus.ACTIVE;
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
        EXPIRED,
        REMOVED_BY_ADMIN
    }
}
