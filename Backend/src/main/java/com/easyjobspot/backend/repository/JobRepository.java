package com.easyjobspot.backend.repository;

import com.easyjobspot.backend.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {

    // ====================================================
    // PUBLIC — ONLY ACTIVE JOBS
    // ====================================================
    Page<Job> findByStatus(Job.JobStatus status, Pageable pageable);

    Page<Job> findByStatusAndCategory(
            Job.JobStatus status,
            String category,
            Pageable pageable
    );

    // ====================================================
    // SEARCH — ACTIVE JOBS ONLY
    // ====================================================
    @Query("""
        SELECT j FROM Job j
        WHERE j.status = :status
        AND (
            LOWER(j.title) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(j.company) LIKE LOWER(CONCAT('%', :search, '%'))
            OR LOWER(j.location) LIKE LOWER(CONCAT('%', :search, '%'))
        )
    """)
    Page<Job> searchByStatus(
            @Param("status") Job.JobStatus status,
            @Param("search") String search,
            Pageable pageable
    );

    // ====================================================
    // ADMIN — JOB MODERATION
    // ====================================================
    Page<Job> findByStatusOrderByCreatedAtDesc(
            Job.JobStatus status,
            Pageable pageable
    );

    // ====================================================
    // SCHEDULER — AUTO EXPIRY
    // ====================================================
    List<Job> findByStatusAndDeadlineBefore(
            Job.JobStatus status,
            LocalDateTime deadline
    );

    // ====================================================
    // DUPLICATE CHECK (SAME PROVIDER ONLY)
    // ====================================================
    boolean existsByTitleIgnoreCaseAndCompanyIgnoreCaseAndLocationIgnoreCaseAndJobTypeAndCreatedBy(
            String title,
            String company,
            String location,
            Job.JobType jobType,
            UUID createdBy
    );

    // ====================================================
    // DASHBOARD — PHASE 10
    // ====================================================
    long countByStatus(Job.JobStatus status);

    // ====================================================
    // DASHBOARD — PHASE 11 (TIME-BASED)
    // ====================================================

    /**
     * Jobs created after a given date (trend analysis)
     */
    long countByCreatedAtAfter(LocalDateTime date);

    /**
     * Jobs approved (ACTIVE) after a given date
     */
    long countByStatusAndCreatedAtAfter(
            Job.JobStatus status,
            LocalDateTime date
    );
}
