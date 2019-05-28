/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dao;

import com.bsi.sec.domain.SSOConfiguration;
import com.bsi.sec.dto.SSOConfigDTO;
import com.bsi.sec.svc.EntityIDGenerator;
import static com.bsi.sec.util.CacheConstants.SSO_CONFIGURATION_CACHE;
import com.bsi.sec.util.JpaQueries;
import java.util.ArrayList;
import java.util.List;
import javax.cache.Cache;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author vnaik
 */
@Component
public class SSOConfigurationDao {
    private final static Logger log = LoggerFactory.getLogger(SSOConfigurationDao.class);

    @Autowired
    private Ignite ignite;

    @Autowired
    private EntityIDGenerator idGen;
    
    /**
     * getSSOConfigsByTenant
     * @param accountName
     * @return 
     */
    public List<SSOConfigDTO> getSSOConfigsByTenant(String accountName){
       List<SSOConfigDTO> configs = new ArrayList<>();
       IgniteCache<Long, SSOConfiguration> ssoConfigCache = ignite.cache(SSO_CONFIGURATION_CACHE);
       SqlQuery sqlQry = new SqlQuery(SSOConfiguration.class, JpaQueries.SELECT_CONFIGS_BY_TENANT);
       try (QueryCursor<Cache.Entry<Long, SSOConfiguration>> cursor = ssoConfigCache.query(sqlQry)) {
           for (Cache.Entry<Long, SSOConfiguration> cf : cursor) {
               SSOConfiguration config = cf.getValue();
               if(config.getTenant() !=null && config.getTenant().getAcctName().equalsIgnoreCase(accountName)){
                   SSOConfigDTO cfg = new SSOConfigDTO();
                   cfg.setAcctName(config.getTenant().getAcctName());
                   cfg.setAcctId(config.getTenant().getId());
                   cfg.setDsplName(config.getDsplName());
                   configs.add(cfg);
               }
           }
       }
       return configs;
    }
}
