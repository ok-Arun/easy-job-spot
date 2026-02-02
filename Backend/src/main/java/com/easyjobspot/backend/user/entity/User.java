package com.easyjobspot.backend.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    /**
     * Security authorization
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    /**
     * Business identity
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private UserType userType;

    /**
     * Provider approval lifecycle
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ProviderStatus providerStatus;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Builder.Default
    private boolean profileCompleted = false;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum Role {
        USER,
        ADMIN
    }

    public enum UserType {
        JOB_SEEKER,
        JOB_PROVIDER,
        SYSTEM_ADMIN
    }

    public enum ProviderStatus {
        PENDING,
        APPROVED,
        REJECTED
    }
}
