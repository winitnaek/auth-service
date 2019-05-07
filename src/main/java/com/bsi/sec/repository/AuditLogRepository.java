package com.bsi.sec.repository;

import com.bsi.sec.domain.AuditLog;
import static com.bsi.sec.util.CacheConstants.AUDIT_LOG_CACHE;
import org.apache.ignite.springdata20.repository.IgniteRepository;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AuditLog entity.
 */
@RepositoryConfig(cacheName = AUDIT_LOG_CACHE)
@Repository
public interface AuditLogRepository extends IgniteRepository<AuditLog, Long> {

}
