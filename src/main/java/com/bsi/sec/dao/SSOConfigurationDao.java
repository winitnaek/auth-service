/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dao;

import com.bsi.sec.domain.SSOConfiguration;
import com.bsi.sec.domain.Tenant;
import com.bsi.sec.dto.SSOConfigDTO;
import com.bsi.sec.repository.SSOConfigurationRepository;
import com.bsi.sec.svc.AuditLogger;
import com.bsi.sec.svc.EntityIDGenerator;
import static com.bsi.sec.util.CacheConstants.SSO_CONFIGURATION_CACHE;
import com.bsi.sec.util.JpaQueries;
import java.util.ArrayList;
import java.util.List;
import javax.cache.Cache;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
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
    
    @Autowired
    private SSOConfigurationRepository ssoConfigurationRepository;
    
    @Autowired
    TenantDao tenantDao;
    
    @Autowired
    private AuditLogger auditLogger;
    
    /**
     * getSSOConfigsByTenant
     * @param accountName
     * @return 
     */
    public List<SSOConfigDTO> getSSOConfigsByTenant(String accountName){
       List<SSOConfigDTO> configs = new ArrayList<>();
       IgniteCache<Long, SSOConfiguration> ssoConfigCache = ignite.cache(SSO_CONFIGURATION_CACHE);
       SqlQuery sqlQry = new SqlQuery(SSOConfiguration.class, JpaQueries.SELECT_CONFIGS);
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
    
    /**
     * getSSOConfById
     *
     * @param id
     * @return
     */
    public SSOConfiguration getSSOConfById(Long id) {
        IgniteCache<Long, SSOConfiguration> ssoConfCache = ignite.cache(SSO_CONFIGURATION_CACHE);
        SqlQuery sqlQry = new SqlQuery(SSOConfiguration.class, "id= ?");
        SSOConfiguration ssoConf = null;
        try (QueryCursor<Cache.Entry<Long, SSOConfiguration>> cursor = ssoConfCache.query(sqlQry.setArgs(id))) {
            for (Cache.Entry<Long, SSOConfiguration> cnf : cursor) {
                ssoConf = cnf.getValue();
            }
        }
        return ssoConf;
    }
    
   /**
    * deleteSSOConfById
    * @param id
    * @return 
    */
    public boolean deleteSSOConfById(Long id) {
       boolean isDeleted = false;
       IgniteCache<Long, SSOConfiguration> cache = ignite.cache(SSO_CONFIGURATION_CACHE);
       cache.query(new SqlFieldsQuery(JpaQueries.DELETE_SSO_CONFIG).setArgs(id));
       isDeleted = true;
       return isDeleted;
    }
    
    /**
     * getSSOConfigs
     * @return 
     */
    public List<SSOConfigDTO> getSSOConfigs() {
        List<SSOConfigDTO> configs = new ArrayList<>();
        IgniteCache<Long, SSOConfiguration> ssoConfigCache = ignite.cache(SSO_CONFIGURATION_CACHE);
        SqlQuery sqlQry = new SqlQuery(SSOConfiguration.class, JpaQueries.SELECT_CONFIGS);
        try (QueryCursor<Cache.Entry<Long, SSOConfiguration>> cursor = ssoConfigCache.query(sqlQry)) {
            for (Cache.Entry<Long, SSOConfiguration> cf : cursor) {
                SSOConfigDTO config = prepareConfig(cf.getValue());
                configs.add(config);
            }
        }
        return configs;
    }
    
    /**
     * prepareConfig
     *
     * @param sSOConfiguration
     * @return
     */
    private SSOConfigDTO prepareConfig(SSOConfiguration sSOConfiguration) {
        SSOConfigDTO config = new SSOConfigDTO();
        config.setId(sSOConfiguration.getId());
        if (sSOConfiguration.getTenant() != null) {
            config.setAcctName(sSOConfiguration.getTenant().getAcctName());
        }
        config.setDsplName(sSOConfiguration.getDsplName());
        config.setEnabled(sSOConfiguration.isEnabled());
        config.setAllowLogout(sSOConfiguration.isAllowLogout());
        config.setAppRedirectURL(sSOConfiguration.getAppRedirectURL());
        config.setAttribIndex(sSOConfiguration.getAttribIndex());
        config.setCertAlias(sSOConfiguration.getCertAlias());
        config.setCertPassword(sSOConfiguration.getCertPassword());
        config.setCertText(sSOConfiguration.getCertText());
        config.setExpireRequestSecs(sSOConfiguration.getExpireRequestSecs());
        config.setIdpIssuer(sSOConfiguration.getIdpIssuer());
        config.setIdpReqURL(sSOConfiguration.getIdpReqURL());
        config.setNonSamlLogoutURL(sSOConfiguration.getNonSamlLogoutURL());
        config.setRedirectToApplication(sSOConfiguration.isRedirectToApplication());
        config.setSpConsumerURL(sSOConfiguration.getSpConsumerURL());
        config.setSpIssuer(sSOConfiguration.getSpIssuer());
        config.setValidateIdpIssuer(sSOConfiguration.isValidateIdpIssuer());
        config.setValidateRespSignature(sSOConfiguration.isValidateRespSignature());
        return config;
    }
    
    /**
     * createSSOConfig
     * @param ssoConfig
     * @return
     * @throws Exception 
     */
    public SSOConfigDTO createSSOConfig(SSOConfigDTO ssoConfig){

        SSOConfiguration sSOConfiguration = new SSOConfiguration();

        sSOConfiguration.setAllowLogout(ssoConfig.getAllowLogout());
        sSOConfiguration.setAppRedirectURL(ssoConfig.getAppRedirectURL());
        sSOConfiguration.setAttribIndex(ssoConfig.getAttribIndex());
        sSOConfiguration.setCertAlias(ssoConfig.getCertAlias());
        sSOConfiguration.setCertPassword(ssoConfig.getCertPassword());
        sSOConfiguration.setCertText(ssoConfig.getCertText());
        sSOConfiguration.setDsplName(ssoConfig.getDsplName());
        sSOConfiguration.setEnabled(ssoConfig.getEnabled());
        sSOConfiguration.setExpireRequestSecs(ssoConfig.getExpireRequestSecs());
        sSOConfiguration.setId(idGen.generate());
        sSOConfiguration.setIdpIssuer(ssoConfig.getIdpIssuer());
        sSOConfiguration.setIdpReqURL(ssoConfig.getIdpReqURL());
        sSOConfiguration.setNonSamlLogoutURL(ssoConfig.getNonSamlLogoutURL());
        sSOConfiguration.setRedirectToApplication(ssoConfig.getRedirectToApplication());
        sSOConfiguration.setSpConsumerURL(ssoConfig.getSpConsumerURL());
        sSOConfiguration.setSpIssuer(ssoConfig.getSpIssuer());

        Tenant tenant = tenantDao.getTenantByName(ssoConfig.getAcctName());
        sSOConfiguration.setTenant(tenant);

        //sSOConfiguration.setTenantSSOConf(tenantSSOConf);
        sSOConfiguration.setValidateIdpIssuer(ssoConfig.getValidateIdpIssuer());
        sSOConfiguration.setValidateRespSignature(ssoConfig.getValidateRespSignature());

        ssoConfigurationRepository.save(sSOConfiguration.getId(), sSOConfiguration);
        auditLogger.logEntity(sSOConfiguration, AuditLogger.Areas.SSO_CONF, AuditLogger.Ops.INSERT);
        return prepareConfig(sSOConfiguration);
    }
    
    /**
     * updateSSOConfig
     * @param ssoConfig
     * @return
     * @throws Exception 
     */
    public SSOConfigDTO updateSSOConfig(SSOConfigDTO ssoConfig) throws Exception{
        SSOConfiguration sSOConfiguration = new SSOConfiguration();
        sSOConfiguration.setAllowLogout(ssoConfig.getAllowLogout());
        sSOConfiguration.setAppRedirectURL(ssoConfig.getAppRedirectURL());
        sSOConfiguration.setAttribIndex(ssoConfig.getAttribIndex());
        sSOConfiguration.setCertAlias(ssoConfig.getCertAlias());
        sSOConfiguration.setCertPassword(ssoConfig.getCertPassword());
        sSOConfiguration.setCertText(ssoConfig.getCertText());
        sSOConfiguration.setDsplName(ssoConfig.getDsplName());
        sSOConfiguration.setEnabled(ssoConfig.getEnabled());
        sSOConfiguration.setExpireRequestSecs(ssoConfig.getExpireRequestSecs());
        sSOConfiguration.setId(ssoConfig.getId());
        sSOConfiguration.setIdpIssuer(ssoConfig.getIdpIssuer());
        sSOConfiguration.setIdpReqURL(ssoConfig.getIdpReqURL());
        sSOConfiguration.setNonSamlLogoutURL(ssoConfig.getNonSamlLogoutURL());
        sSOConfiguration.setRedirectToApplication(ssoConfig.getRedirectToApplication());

        sSOConfiguration.setSpConsumerURL(ssoConfig.getSpConsumerURL());
        sSOConfiguration.setSpIssuer(ssoConfig.getSpIssuer());

        Tenant tenant = tenantDao.getTenantByName(ssoConfig.getAcctName());
        sSOConfiguration.setTenant(tenant);

        //sSOConfiguration.setTenantSSOConf(tenantSSOConf);
        sSOConfiguration.setValidateIdpIssuer(ssoConfig.getValidateIdpIssuer());
        sSOConfiguration.setValidateRespSignature(ssoConfig.getValidateRespSignature());

        ssoConfigurationRepository.save(sSOConfiguration.getId(), sSOConfiguration);
        auditLogger.logEntity(sSOConfiguration, AuditLogger.Areas.SSO_CONF, AuditLogger.Ops.UPDATE);
        return prepareConfig(sSOConfiguration);
    }
}
