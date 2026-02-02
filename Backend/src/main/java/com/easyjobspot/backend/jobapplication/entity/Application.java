package com.easyjobspot.backend.jobapplication.entity;


import com.easyjobspot.backend.job.entity.Job;
import com.easyjobspot.backend.jobapplication.enums.ApplicationStatus;
import com.easyjobspot.backend.user.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "applications",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "job_id"})
        }
)
public class Application {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ApplicationStatus status;

    @Column(length = 1000)
    private String rejectionReason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime appliedAt;

    // ====================================================
    // LIFECYCLE
    // ====================================================
    @PrePersist
    public void prePersist() {
        this.appliedAt = LocalDateTime.now();
        this.status = ApplicationStatus.APPLIED;
    }

    // ====================================================
    // DOMAIN TRANSITIONS
    // ====================================================

    public void shortlist() {
        if (this.status != ApplicationStatus.APPLIED) {
            throw new IllegalStateException("Only applied applications can be shortlisted");
        }
        this.status = ApplicationStatus.SHORTLISTED;
        this.rejectionReason = null;
    }

    public void hire() {
        if (this.status != ApplicationStatus.SHORTLISTED) {
            throw new IllegalStateException("Only shortlisted applications can be hired");
        }
        this.status = ApplicationStatus.HIRED;
        this.rejectionReason = null;
    }

    public void reject(String reason) {
        if (this.status == ApplicationStatus.HIRED) {
            throw new IllegalStateException("Hired candidate cannot be rejected");
        }
        this.status = ApplicationStatus.REJECTED;
        this.rejectionReason = reason;
    }

    // ====================================================
    // GETTERS
    // ====================================================

    public UUID getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Job getJob() {
        return job;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    // ====================================================
    // SETTERS (RESTRICTED)
    // ====================================================

    public void setUser(User user) {
        this.user = user;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
