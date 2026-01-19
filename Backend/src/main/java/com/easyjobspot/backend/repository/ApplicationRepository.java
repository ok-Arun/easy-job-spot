package com.easyjobspot.backend.repository;

import com.easyjobspot.backend.entity.Application;
import com.easyjobspot.backend.entity.Job;
import com.easyjobspot.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    boolean existsByUserAndJob(User user, Job job);

    List<Application> findByUserOrderByAppliedAtDesc(User user);
}