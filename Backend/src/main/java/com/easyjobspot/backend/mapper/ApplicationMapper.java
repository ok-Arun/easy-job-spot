package com.easyjobspot.backend.mapper;

import com.easyjobspot.backend.dto.response.AdminApplicationResponse;
import com.easyjobspot.backend.dto.response.ApplicationResponse;
import com.easyjobspot.backend.entity.Application;

public class ApplicationMapper {

    public static ApplicationResponse toResponse(Application application) {

        ApplicationResponse response = new ApplicationResponse();

        response.setId(application.getId());
        response.setStatus(application.getStatus()); // ✅ enum, not String
        response.setAppliedAt(application.getAppliedAt());
        response.setJobId(application.getJob().getId());
        response.setJobTitle(application.getJob().getTitle());
        response.setCompany(application.getJob().getCompany());
        response.setLocation(application.getJob().getLocation());
        response.setCategory(application.getJob().getCategory());

        return response;
    }

    // ================= ADMIN VIEW =================
    public static AdminApplicationResponse toAdminResponse(Application application) {

        return AdminApplicationResponse.builder()
                .applicantName(application.getUser().getName())
                .applicantEmail(application.getUser().getEmail())
                .appliedAt(application.getAppliedAt())
                .status(application.getStatus().name())
                .build();
    }
}
