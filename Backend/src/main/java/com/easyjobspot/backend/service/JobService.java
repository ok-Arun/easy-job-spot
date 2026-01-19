package com.easyjobspot.backend.service;

import com.easyjobspot.backend.dto.JobDTO;
import com.easyjobspot.backend.dto.JobRequest;
import com.easyjobspot.backend.entity.Job;
import com.easyjobspot.backend.exception.ResourceNotFoundException;
import com.easyjobspot.backend.mapper.JobMapper;
import com.easyjobspot.backend.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    @Transactional(readOnly = true)
    public Page<JobDTO> getAllJobs(int page, int size, String sortBy, String search, String category) {
        log.info("Fetching jobs - page: {}, size: {}, sortBy: {}, search: {}, category: {}",
                page, size, sortBy, search, category);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
        Page<Job> jobs;

        if (search != null && !search.trim().isEmpty()) {
            jobs = jobRepository.searchJobs(search.trim(), pageable);
        } else if (category != null && !category.trim().isEmpty()) {
            jobs = jobRepository.findByCategory(category.trim(), pageable);
        } else {
            jobs = jobRepository.findAll(pageable);
        }

        return jobs.map(jobMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public JobDTO getJobById(UUID id) {
        log.info("Fetching job with id: {}", id);

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        return jobMapper.toDTO(job);
    }

    @Transactional(readOnly = true)
    public Page<JobDTO> getJobsByCategory(String category, int page, int size) {
        log.info("Fetching jobs by category: {} - page: {}, size: {}", category, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Job> jobs = jobRepository.findByCategory(category, pageable);

        return jobs.map(jobMapper::toDTO);
    }

    @Transactional
    public JobDTO createJob(JobRequest request) {
        log.info("Creating new job with title: {}", request.getTitle());

        Job job = jobMapper.toEntity(request);
        Job savedJob = jobRepository.save(job);

        log.info("Job created successfully with id: {}", savedJob.getId());
        return jobMapper.toDTO(savedJob);
    }

    @Transactional
    public JobDTO updateJob(UUID id, JobRequest request) {
        log.info("Updating job with id: {}", id);

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id: " + id));

        jobMapper.updateEntity(request, job);
        Job updatedJob = jobRepository.save(job);

        log.info("Job updated successfully with id: {}", id);
        return jobMapper.toDTO(updatedJob);
    }

    @Transactional
    public void deleteJob(UUID id) {
        log.info("Deleting job with id: {}", id);

        if (!jobRepository.existsById(id)) {
            throw new ResourceNotFoundException("Job not found with id: " + id);
        }

        jobRepository.deleteById(id);
        log.info("Job deleted successfully with id: {}", id);
    }
}