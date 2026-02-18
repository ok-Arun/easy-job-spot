package com.easyjobspot.backend.dashboard.admin.dto;

import com.easyjobspot.backend.jobapplication.enums.ApplicationStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AdminJobApplicationView {

    private UUID applicationId;
    private UUID applicantId;
    private String applicantName;
    private String applicantEmail;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;
}
