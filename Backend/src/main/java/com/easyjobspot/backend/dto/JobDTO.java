package com.easyjobspot.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

// Job Response DTO
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDTO {
    private UUID id;
    private String title;
    private String company;
    private String category;
    private String location;
    private String jobType;
    private String description;
    private LocalDateTime createdAt;
}