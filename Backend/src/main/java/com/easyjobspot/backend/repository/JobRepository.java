package com.easyjobspot.backend.repository;

import com.easyjobspot.backend.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, UUID> {

    Page<Job> findByCategory(String category, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE " +
            "LOWER(j.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(j.company) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(j.location) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Job> searchJobs(@Param("search") String search, Pageable pageable);
}