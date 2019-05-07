package com.bsi.sec.repository;

import com.bsi.sec.domain.Company;
import static com.bsi.sec.util.CacheConstants.COMPANY_CACHE;
import org.apache.ignite.springdata20.repository.IgniteRepository;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the Company entity.
 */
@RepositoryConfig(cacheName = COMPANY_CACHE)
@Repository
public interface CompanyRepository extends IgniteRepository<Company, Long> {

}
