package com.easyjobspot.backend.job.repository;

import com.easyjobspot.backend.job.entity.Job;
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

    // ================= PUBLIC — ONLY ACTIVE JOBS =================
    Page<Job> findByStatus(Job.JobStatus status, Pageable pageable);

    Page<Job> findByStatusAndCategory(
            Job.JobStatus status,
            String category,
            Pageable pageable
    );

    // ================= SEARCH — ACTIVE JOBS ONLY =================
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

    // ================= FILTER BY TITLE + LOCATION =================
    @Query("""
        SELECT j FROM Job j
        WHERE j.status = :status
        AND (:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%')))
        AND (:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%')))
    """)
    Page<Job> filterByTitleAndLocation(
            @Param("status") Job.JobStatus status,
            @Param("title") String title,
            @Param("location") String location,
            Pageable pageable
    );

    // ================= ADMIN — MODERATION =================
    Page<Job> findByStatusOrderByCreatedAtDesc(
            Job.JobStatus status,
            Pageable pageable
    );

    // ================= SCHEDULER — AUTO EXPIRY =================
    List<Job> findByStatusAndDeadlineBefore(
            Job.JobStatus status,
            LocalDateTime deadline
    );

    // ================= DUPLICATE CHECK =================
    boolean existsByTitleIgnoreCaseAndCompanyIgnoreCaseAndLocationIgnoreCaseAndJobTypeAndCreatedBy(
            String title,
            String company,
            String location,
            Job.JobType jobType,
            UUID createdBy
    );

    // ================= DASHBOARD — GLOBAL =================
    long countByStatus(Job.JobStatus status);

    // ================= DASHBOARD — TIME BASED =================
    long countByCreatedAtAfter(LocalDateTime date);

    long countByStatusAndCreatedAtAfter(
            Job.JobStatus status,
            LocalDateTime date
    );

    // ================= DASHBOARD — PROVIDER =================
    long countByCreatedBy(UUID createdBy);

    long countByStatusAndCreatedBy(
            Job.JobStatus status,
            UUID createdBy
    );

    // ================= PROVIDER — BASIC JOB LIST =================
    Page<Job> findByCreatedBy(UUID createdBy, Pageable pageable);

    Page<Job> findByCreatedByAndStatus(
            UUID createdBy,
            Job.JobStatus status,
            Pageable pageable
    );


    // =========================================================
    // ⭐ CRITICAL FIX — JOBS WITH APPLICATION COUNT
    // =========================================================
    @Query("""
        SELECT
            j.id,
            j.title,
            j.company,
            j.category,
            j.location,
            j.jobType,
            j.status,
            j.createdAt,
            COUNT(a.id)
        FROM Job j
        LEFT JOIN Application a ON a.job = j
        WHERE j.createdBy = :providerId
        AND (:status IS NULL OR j.status = :status)
        GROUP BY
            j.id, j.title, j.company, j.category,
            j.location, j.jobType, j.status, j.createdAt
        ORDER BY j.createdAt DESC
    """)
    List<Object[]> fetchProviderJobsWithApplicationCount(
            @Param("providerId") UUID providerId,
            @Param("status") Job.JobStatus status
    );
}
