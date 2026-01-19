package com.easyjobspot.backend.service;

import com.easyjobspot.backend.dto.ApplicationDTO;
import com.easyjobspot.backend.entity.Application;
import com.easyjobspot.backend.entity.Job;
import com.easyjobspot.backend.entity.User;
import com.easyjobspot.backend.exception.DuplicateResourceException;
import com.easyjobspot.backend.exception.ResourceNotFoundException;
import com.easyjobspot.backend.mapper.ApplicationMapper;
import com.easyjobspot.backend.repository.ApplicationRepository;
import com.easyjobspot.backend.repository.JobRepository;
import com.easyjobspot.backend.repository.UserRepository;
import com.easyjobspot.backend.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationMapper applicationMapper;

    @Transactional
    public ApplicationDTO applyForJob(UUID jobId, UserPrincipal currentUser) {
        log.info("User {} applying for job {}", currentUser.getId(), jobId);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + jobId));

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (applicationRepository.existsByUserAndJob(user, job)) {
            log.error("User {} already applied for job {}", currentUser.getId(), jobId);
            throw new DuplicateResourceException("You have already applied for this job");
        }

        Application application = Application.builder()
                .user(user)
                .job(job)
                .status(Application.Status.APPLIED)
                .build();

        Application savedApplication = applicationRepository.save(application);
        log.info("Application created successfully with id: {}", savedApplication.getId());

        return applicationMapper.toDTO(savedApplication);
    }

    @Transactional(readOnly = true)
    public List<ApplicationDTO> getMyApplications(UserPrincipal currentUser) {
        log.info("Fetching applications for user: {}", currentUser.getId());

        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Application> applications = applicationRepository.findByUserOrderByAppliedAtDesc(user);

        return applications.stream()
                .map(applicationMapper::toDTO)
                .collect(Collectors.toList());
    }
}