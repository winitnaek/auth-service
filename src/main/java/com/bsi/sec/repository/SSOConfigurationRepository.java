package com.bsi.sec.repository;

import com.bsi.sec.domain.SSOConfiguration;
import static com.bsi.sec.util.CacheConstants.SSO_CONFIGURATION_CACHE;
import org.apache.ignite.springdata20.repository.IgniteRepository;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;

/**
 * Spring Data repository for the SSOConfiguration entity.
 */
@RepositoryConfig(cacheName = SSO_CONFIGURATION_CACHE)
public interface SSOConfigurationRepository extends IgniteRepository<SSOConfiguration, Long> {

}
