package org.j2os.repository;

import org.j2os.entity.AuditLog;

public interface AuditRepository {
    void save(AuditLog auditLog);
}
