package com.bsi.sec.repository;

import com.bsi.sec.domain.AdminMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AdminMetadata entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminMetadataRepository extends JpaRepository<AdminMetadata, Long> {

}
