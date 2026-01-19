package com.easyjobspot.backend.dto;

import com.easyjobspot.backend.entity.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationDTO {
    private UUID id;
    private JobDTO job;
    private LocalDateTime appliedAt;
    private Application.Status status;
}