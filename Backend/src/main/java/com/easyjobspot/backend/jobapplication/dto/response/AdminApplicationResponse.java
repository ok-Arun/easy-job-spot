package com.easyjobspot.backend.jobapplication.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AdminApplicationResponse {

    private UUID applicationId;

    private String applicantName;
    private String applicantEmail;

    private LocalDateTime appliedAt;

    private String status;

    private String rejectionReason;
}
