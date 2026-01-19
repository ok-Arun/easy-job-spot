package com.easyjobspot.backend.mapper;

import com.easyjobspot.backend.dto.ApplicationDTO;
import com.easyjobspot.backend.entity.Application;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationMapper {

    private final JobMapper jobMapper;

    public ApplicationDTO toDTO(Application application) {
        if (application == null) {
            return null;
        }

        return ApplicationDTO.builder()
                .id(application.getId())
                .job(jobMapper.toDTO(application.getJob()))
                .appliedAt(application.getAppliedAt())
                .status(application.getStatus())
                .build();
    }
}