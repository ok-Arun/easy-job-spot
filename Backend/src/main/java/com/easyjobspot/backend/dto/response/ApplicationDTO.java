package com.easyjobspot.backend.dto.response;

import com.easyjobspot.backend.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ApplicationDTO {

    private UUID id;
    private JobDTO job;
    private LocalDateTime appliedAt;
    private ApplicationStatus status;
}
