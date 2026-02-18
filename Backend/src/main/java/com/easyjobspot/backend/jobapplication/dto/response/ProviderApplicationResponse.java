package com.easyjobspot.backend.jobapplication.dto.response;

import com.easyjobspot.backend.jobapplication.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ProviderApplicationResponse {

    private UUID id;
    private String applicantName;
    private String applicantEmail;
    private LocalDateTime appliedAt;
    private ApplicationStatus status;
}
