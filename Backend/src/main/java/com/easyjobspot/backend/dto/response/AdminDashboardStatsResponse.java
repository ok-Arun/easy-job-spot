package com.easyjobspot.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AdminDashboardStatsResponse {

    private JobStats jobs;
    private ApplicationStats applications;
    private List<JobApplicationCountDTO> perJobApplications;

    @Getter
    @Builder
    public static class JobStats {
        private long total;
        private long pending;
        private long active;
    }

    @Getter
    @Builder
    public static class ApplicationStats {
        private long total;
        private long shortlisted;
        private long rejected;
        private long hired;
    }
}
