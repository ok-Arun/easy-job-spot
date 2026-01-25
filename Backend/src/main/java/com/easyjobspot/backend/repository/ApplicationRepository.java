package com.easyjobspot.backend.repository;

import com.easyjobspot.backend.entity.Application;
import com.easyjobspot.backend.entity.Job;
import com.easyjobspot.backend.entity.User;
import com.easyjobspot.backend.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
    // OPTIONAL (FOR DASHBOARD / FUTURE)
    // ====================================================
    long countByJobIdAndStatus(UUID jobId, ApplicationStatus status);
}
