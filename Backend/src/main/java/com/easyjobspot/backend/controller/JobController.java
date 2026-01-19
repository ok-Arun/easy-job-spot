package com.easyjobspot.backend.controller;

import com.easyjobspot.backend.dto.JobDTO;
import com.easyjobspot.backend.dto.JobRequest;
import com.easyjobspot.backend.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@Slf4j
public class JobController {

    private final JobService jobService;

    @GetMapping
    public ResponseEntity<Page<JobDTO>> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category) {
        log.info("GET /api/jobs - page: {}, size: {}, sortBy: {}, search: {}, category: {}",
                page, size, sortBy, search, category);
        Page<JobDTO> jobs = jobService.getAllJobs(page, size, sortBy, search, category);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable UUID id) {
        log.info("GET /api/jobs/{}", id);
        JobDTO job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<JobDTO>> getJobsByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/jobs/category/{} - page: {}, size: {}", category, page, size);
        Page<JobDTO> jobs = jobService.getJobsByCategory(category, page, size);
        return ResponseEntity.ok(jobs);
    }

    @PostMapping
    public ResponseEntity<JobDTO> createJob(@Valid @RequestBody JobRequest request) {
        log.info("POST /api/jobs - Creating job: {}", request.getTitle());
        JobDTO job = jobService.createJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }

    @PutMapping("/{id}")
    public ResponseEntity<JobDTO> updateJob(
            @PathVariable UUID id,
            @Valid @RequestBody JobRequest request) {
        log.info("PUT /api/jobs/{} - Updating job", id);
        JobDTO job = jobService.updateJob(id, request);
        return ResponseEntity.ok(job);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable UUID id) {
        log.info("DELETE /api/jobs/{}", id);
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
