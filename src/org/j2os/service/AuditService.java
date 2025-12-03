package org.j2os.service;

import org.j2os.entity.AuditLog;
import org.j2os.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {
    @Autowired
    private AuditRepository auditRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveAudit(String message){

        AuditLog log = AuditLog.builder()
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();

        auditRepository.save(log);
    }
}
