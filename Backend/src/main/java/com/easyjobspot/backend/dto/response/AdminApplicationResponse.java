package com.easyjobspot.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AdminApplicationResponse {

    private String applicantName;
    private String applicantEmail;
    private LocalDateTime appliedAt;
    private String status;
}
