package com.easyjobspot.backend.audit.service;


import com.easyjobspot.backend.audit.entity.AdminAuditLog;
import com.easyjobspot.backend.audit.enums.AuditAction;
import com.easyjobspot.backend.audit.repository.AdminAuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AdminAuditLogRepository auditLogRepository;

    @Transactional
    public void log(
            UUID adminId,
            AuditAction action,
            UUID targetId,
            String note
    ) {

        AdminAuditLog log = new AdminAuditLog();
        log.setAdminId(adminId);
        log.setAction(action);
        log.setTargetId(targetId);
        log.setNote(note);

        auditLogRepository.save(log);
    }
}
