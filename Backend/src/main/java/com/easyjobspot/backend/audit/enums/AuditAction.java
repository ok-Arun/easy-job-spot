package com.easyjobspot.backend.audit.enums;

public enum AuditAction {

    // ================= JOB ACTIONS =================
    JOB_APPROVED,
    JOB_REJECTED,
    JOB_CLOSED,
    JOB_REMOVED_BY_ADMIN,
    JOB_RESTORED_BY_ADMIN,

    // ================= APPLICATION ACTIONS =================
    APPLICATION_SHORTLISTED,
    APPLICATION_REJECTED,
    APPLICATION_HIRED
}
