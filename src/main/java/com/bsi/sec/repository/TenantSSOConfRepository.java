package com.bsi.sec.repository;

import com.bsi.sec.domain.TenantSSOConf;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TenantSSOConf entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantSSOConfRepository extends JpaRepository<TenantSSOConf, Long> {

}
