package com.easyjobspot.backend.repository;

import com.easyjobspot.backend.entity.Application;
import com.easyjobspot.backend.entity.Job;
import com.easyjobspot.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    boolean existsByUserAndJob(User user, Job job);

    // USER — view own applications
    @Query("""
        SELECT DISTINCT a FROM Application a
        JOIN FETCH a.job
        WHERE a.user = :user
    """)
    List<Application> findApplicationsWithJob(User user);

    // ADMIN — view applicants per job
    @Query("""
        SELECT DISTINCT a FROM Application a
        JOIN FETCH a.user
        WHERE a.job.id = :jobId
    """)
    List<Application> findApplicationsByJobId(UUID jobId);
}
