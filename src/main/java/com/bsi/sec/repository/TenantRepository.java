package com.bsi.sec.repository;

import com.bsi.sec.domain.Tenant;
import static com.bsi.sec.util.CacheConstants.TENANT_CACHE;
import org.apache.ignite.springdata20.repository.IgniteRepository;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Tenant entity.
 */
@RepositoryConfig(cacheName = TENANT_CACHE)
@Repository
public interface TenantRepository extends IgniteRepository<Tenant, Long> {

}
