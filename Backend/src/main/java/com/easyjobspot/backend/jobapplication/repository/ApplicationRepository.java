package com.easyjobspot.backend.jobapplication.repository;

import com.easyjobspot.backend.job.entity.Job;
import com.easyjobspot.backend.jobapplication.entity.Application;
import com.easyjobspot.backend.jobapplication.enums.ApplicationStatus;
import com.easyjobspot.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    // ====================================================
    // DUPLICATE APPLY CHECK
    // ====================================================
    boolean existsByUserAndJob(User user, Job job);

    // ====================================================
    // USER — VIEW OWN APPLICATIONS
    // ====================================================
    @Query("""
        SELECT DISTINCT a FROM Application a
        JOIN FETCH a.job
        WHERE a.user = :user
    """)
    List<Application> findApplicationsWithJob(User user);

    // ====================================================
    // ADMIN — VIEW APPLICANTS PER JOB
    // ====================================================
    @Query("""
        SELECT DISTINCT a FROM Application a
        JOIN FETCH a.user
        WHERE a.job.id = :jobId
    """)
    List<Application> findApplicationsByJobId(UUID jobId);

    // ====================================================
    // ADMIN — FETCH BY ID (SAFE)
    // ====================================================
    Optional<Application> findById(UUID id);

    // ====================================================
    // DASHBOARD — GLOBAL
    // ====================================================
    long countByStatus(ApplicationStatus status);

    @Query("""
        SELECT a.job.id, a.job.title, COUNT(a)
        FROM Application a
        GROUP BY a.job.id, a.job.title
    """)
    List<Object[]> countApplicationsPerJob();

    long countByJobIdAndStatus(UUID jobId, ApplicationStatus status);

    // ====================================================
    // DASHBOARD — TIME BASED
    // ====================================================
    long countByAppliedAtAfter(LocalDateTime date);

    long countByStatusAndAppliedAtAfter(
            ApplicationStatus status,
            LocalDateTime date
    );

    // ====================================================
    // DASHBOARD — PROVIDER SCOPED
    // ====================================================

    @Query("""
        SELECT COUNT(a)
        FROM Application a
        JOIN a.job j
        WHERE j.createdBy = :providerId
    """)
    long countAllByProvider(UUID providerId);

    @Query("""
        SELECT COUNT(a)
        FROM Application a
        JOIN a.job j
        WHERE j.createdBy = :providerId
        AND a.status = :status
    """)
    long countByStatusAndProvider(
            ApplicationStatus status,
            UUID providerId
    );

    // ====================================================
    // PROVIDER DASHBOARD — PER JOB BREAKDOWN (FIXED)
    // ====================================================
    @Query("""
        SELECT
            j.id,
            j.title,
            j.status,
            COUNT(a.id),
            SUM(CASE WHEN a.status = 'SHORTLISTED' THEN 1 ELSE 0 END),
            SUM(CASE WHEN a.status = 'REJECTED' THEN 1 ELSE 0 END),
            SUM(CASE WHEN a.status = 'HIRED' THEN 1 ELSE 0 END)
        FROM Job j
        LEFT JOIN Application a ON a.job = j
        WHERE j.createdBy = :providerId
        GROUP BY j.id, j.title, j.status
        ORDER BY j.createdAt DESC
    """)
    List<Object[]> fetchPerJobApplicationStats(UUID providerId);
}
