package com.bsi.sec.repository;

import com.bsi.sec.domain.TenantSSOConf;
import static com.bsi.sec.util.CacheConstants.TENANT_SSO_CONF_CACHE;
import org.apache.ignite.springdata20.repository.IgniteRepository;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the TenantSSOConf entity.
 */
@RepositoryConfig(cacheName = TENANT_SSO_CONF_CACHE)
@Repository
public interface TenantSSOConfRepository extends IgniteRepository<TenantSSOConf, Long> {

}
