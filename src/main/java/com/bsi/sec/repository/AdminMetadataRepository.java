package com.bsi.sec.repository;

import com.bsi.sec.domain.AdminMetadata;
import static com.bsi.sec.util.CacheConstants.ADMIN_METADATA_CACHE;
import org.apache.ignite.springdata20.repository.IgniteRepository;
import org.apache.ignite.springdata20.repository.config.RepositoryConfig;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for the AdminMetadata entity.
 */
@RepositoryConfig(cacheName = ADMIN_METADATA_CACHE)
@Repository
public interface AdminMetadataRepository extends IgniteRepository<AdminMetadata, Long> {
}
