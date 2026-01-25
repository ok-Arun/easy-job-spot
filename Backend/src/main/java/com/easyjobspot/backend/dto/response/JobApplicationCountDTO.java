package com.easyjobspot.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class JobApplicationCountDTO {
    private UUID jobId;
    private String jobTitle;
    private long applicationCount;
}
