package com.bsi.sec.repository;

import com.bsi.sec.domain.Company;
import static com.bsi.sec.util.CacheConstants.COMPANY_CACHE;
import org.apache.ignite.springdata20.repository.IgniteRepository;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;

/**
 * Spring Data repository for the Company entity.
 */
@RepositoryConfig(cacheName = COMPANY_CACHE)
public interface CompanyRepository extends IgniteRepository<Company, Long> {

}
