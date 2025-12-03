package org.j2os.service;

import org.j2os.entity.AuditLog;
import org.j2os.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HelperService {
    @Autowired
    private AuditRepository auditRepository;

    // ------------------------- MANDATORY -------------------------
    public void doMandatoryWork(){
        auditRepository.save(AuditLog.builder()
                .message("MANDATORY executed")
                .timestamp(System.currentTimeMillis())
                .build());
    }

    // ------------------------- SUPPORTS -------------------------
    @Transactional(propagation = Propagation.SUPPORTS)
    public void doSupportsWork() {
        auditRepository.save(AuditLog.builder()
                .message("SUPPORTS executed")
                .timestamp(System.currentTimeMillis())
                .build());
    }

    // ------------------------- NESTED -------------------------
    @Transactional(propagation = Propagation.NESTED)
    public void doNestedWork() {
        auditRepository.save(AuditLog.builder()
                .message("NESTED executed")
                .timestamp(System.currentTimeMillis())
                .build());

        // مثال Rollback فقط روی nested transaction
        // Transaction اصلی rollback نمی‌شود
        // throw new RuntimeException("Nested failure");
    }

    // ------------------------- NEVER -------------------------
    @Transactional(propagation = Propagation.NEVER)
    public void doNeverWork() {
        auditRepository.save(AuditLog.builder()
                .message("NEVER executed")
                .timestamp(System.currentTimeMillis())
                .build());
    }
}
