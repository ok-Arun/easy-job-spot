package com.easyjobspot.backend.audit.repository;


import com.easyjobspot.backend.audit.entity.AdminAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminAuditLogRepository extends JpaRepository<AdminAuditLog, UUID> {
}
