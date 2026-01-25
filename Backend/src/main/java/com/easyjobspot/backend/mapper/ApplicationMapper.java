package com.easyjobspot.backend.mapper;

import com.easyjobspot.backend.dto.response.AdminApplicationResponse;
import com.easyjobspot.backend.dto.response.ApplicationResponse;
import com.easyjobspot.backend.entity.Application;

public class ApplicationMapper {

    // ====================================================
    // JOB SEEKER VIEW
    // ====================================================
    public static ApplicationResponse toResponse(Application application) {

        ApplicationResponse response = new ApplicationResponse();

        response.setId(application.getId());
        response.setStatus(application.getStatus()); // enum
        response.setAppliedAt(application.getAppliedAt());
        response.setJobId(application.getJob().getId());
        response.setJobTitle(application.getJob().getTitle());
        response.setCompany(application.getJob().getCompany());
        response.setLocation(application.getJob().getLocation());
        response.setCategory(application.getJob().getCategory());

        return response;
    }

    // ====================================================
    // ADMIN VIEW
    // ====================================================
    public static AdminApplicationResponse toAdminResponse(Application application) {

        return AdminApplicationResponse.builder()
                .applicationId(application.getId())
                .applicantName(application.getUser().getName())
                .applicantEmail(application.getUser().getEmail())
                .appliedAt(application.getAppliedAt())
                .status(application.getStatus().name())
                .rejectionReason(application.getRejectionReason())
                .build();
    }
}
