package com.easyjobspot.backend.dto.response;

import com.easyjobspot.backend.entity.Job;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
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
