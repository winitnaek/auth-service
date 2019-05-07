package com.bsi.sec.repository;

import com.bsi.sec.domain.Tenant;
import static com.bsi.sec.util.CacheConstants.TENANT_CACHE;
import org.apache.ignite.springdata20.repository.IgniteRepository;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;

/**
 * Spring Data repository for the Tenant entity.
 */
@RepositoryConfig(cacheName = TENANT_CACHE)
public interface TenantRepository extends IgniteRepository<Tenant, Long> {

}
