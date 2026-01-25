package com.easyjobspot.backend.entity;

import com.easyjobspot.backend.enums.ApplicationStatus;
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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "job_id")
    private Job job;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;

    @Column(nullable = false)
    private LocalDateTime appliedAt;

    public Application() {
    }

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

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }
}
