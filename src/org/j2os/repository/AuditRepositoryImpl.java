package org.j2os.repository;

import org.j2os.entity.AuditLog;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class AuditRepositoryImpl implements AuditRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(AuditLog auditLog) {
        entityManager.persist(auditLog);
    }
}
