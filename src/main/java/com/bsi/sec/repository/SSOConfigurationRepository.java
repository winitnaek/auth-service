package com.bsi.sec.repository;

import com.bsi.sec.domain.SSOConfiguration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SSOConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SSOConfigurationRepository extends JpaRepository<SSOConfiguration, Long> {

}
