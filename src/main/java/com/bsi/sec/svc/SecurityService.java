/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.dao.AdminMetadataDao;
import com.bsi.sec.dao.CompanyDao;
import com.bsi.sec.dao.TenantDao;
import com.bsi.sec.domain.AdminMetadata;
import com.bsi.sec.domain.Tenant;
import com.bsi.sec.domain.AuditLog;
import com.bsi.sec.domain.Company;
import com.bsi.sec.domain.SSOConfiguration;
import com.bsi.sec.dto.AuditLogDTO;
import com.bsi.sec.dto.DatasetProductDTO;
import com.bsi.sec.dto.ProductDTO;
import com.bsi.sec.dto.SSOConfigDTO;
import com.bsi.sec.dto.SyncInfoDTO;
import com.bsi.sec.dto.TenantDTO;
import com.bsi.sec.exception.ProcessingException;
import com.bsi.sec.exception.RecordNotFoundException;
import com.bsi.sec.repository.AdminMetadataRepository;
import com.bsi.sec.repository.CompanyRepository;
import com.bsi.sec.repository.SSOConfigurationRepository;
import com.bsi.sec.repository.TenantRepository;
import static com.bsi.sec.util.CacheConstants.TENANT_CACHE;
import static com.bsi.sec.util.CacheConstants.AUDIT_LOG_CACHE;
import static com.bsi.sec.util.CacheConstants.SSO_CONFIGURATION_CACHE;
import com.bsi.sec.util.DateUtils;
import com.bsi.sec.util.LogUtils;
import com.bsi.sec.util.JpaQueries;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for serving REST resource/controller requests!
 *
 * @author igorV
 */
@Service
@Transactional
public class SecurityService {
    
    private final static Logger log = LoggerFactory.getLogger(SecurityService.class);
    
    @Autowired
    private AsyncInitialDataSyncJob initialDataSyncJob;
    
    @Autowired
    private AsyncPeriodicDataSyncJob periodicDataSyncJob;
    
    @Autowired
    Ignite igniteInstance;
    
    @Autowired
    private EntityIDGenerator idGenerator;
    
    @Autowired
    private SSOConfigurationRepository ssoConfigurationRepository;
    
    @Autowired
    private AuditLogger auditLogger;
    
    @Autowired
    private AdminMetadataRepository adminMetaRepo;
    
    @Autowired
    private AdminMetadataDao adminMetaDao;
    
    @Autowired
    private TenantDao tenantDao;
    
    @Autowired
    private TenantRepository tenantRepo;
    
    @Autowired
    private CompanyDao compDao;
    
    @Autowired
    private CompanyRepository compRepo;

    /**
     *
     * @param dataset
     * @return
     * @throws Exception
     */
    public Set<DatasetProductDTO> getProductsByDataset(String dataset) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("SERVICE invoked to get all Products for the specfied Dataset.");
        }

        // TODO: This is a service stub!!! Provide actual implementation!!!
        if (log.isDebugEnabled()) {
            log.debug("This is a service stub!!! Provide actual implementation!!!");
        }
        
        Set<DatasetProductDTO> productsToRet = getTestProducts();
        return productsToRet;
    }

    /**
     * Full Data Sync service
     *
     * @return
     */
    public boolean runFullSFSync() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("SERVICE invoked to run Full Salesforce sync.");
        }
        
        initialDataSyncJob.run(DateUtils.defaultFromSyncTime(), true);
        
        boolean isFullSFSyncSuccess = true;
        return isFullSFSyncSuccess;
    }

    /**
     * Periodic Data Sync service
     *
     * @param fromDateTime
     * @return
     */
    public boolean runPeriodicDataSync(LocalDateTime fromDateTime) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("SERVICE invoked to run Periodic Salesforce sync with args: fromDateTime -> {}",
                    fromDateTime.toInstant(ZoneOffset.UTC).toString());
        }
        
        periodicDataSyncJob.run(fromDateTime, true);
        
        boolean isPerSFSyncSuccess = true;
        return isPerSFSyncSuccess;
    }

    /**
     *
     * @param enabled
     * @return
     */
    public boolean enablePeriodicDataSync(boolean enabled) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("SERVICE invoked to enable/disable Periodic Data Sync with args: enabled -> {}",
                    Boolean.valueOf(enabled).toString());
        }
        
        AdminMetadata ent = adminMetaDao.get();
        ent.setIsPerSyncOn(enabled);
        AdminMetadata updEnt = adminMetaRepo.save(ent.getId(), ent);
        
        if (updEnt != null) {
            auditLogger.logEntity(ent, AuditLogger.Areas.ADMIN_META_DATA,
                    AuditLogger.Ops.UPDATE);
            
            if (log.isInfoEnabled()) {
                log.info(LogUtils.jsonize("Periodic Data Sync configuration was updated.",
                        "rec", updEnt.toString()));
            }
            
            return true;
        } else {
            throw new ProcessingException("Failed to update Periodic Data Sync flag!");
        }
    }

    /**
     *
     * @param accountName
     * @param productName
     * @param datasetName
     * @return
     */
    public TenantDTO createTenant(String acctName, String prodName,
            String dset, String companyCID) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("SERVICE invoked to add new Tenant with Account Name = {}, "
                    + "Product Name = {}, Dataset Name = {}, Company CID = {}",
                    acctName, prodName, dset, companyCID);
        }
        
        Tenant tenantEnt = tenantDao.getTenantByDsetProdAcct(dset, prodName,
                acctName, true);
        Set<Company> companies = new HashSet<>(0);
        
        if (tenantEnt == null) {
            if (StringUtils.isNotBlank(companyCID)) {
                Company company = saveCompany(dset, companyCID);
                auditLogger.logEntity(company, AuditLogger.Areas.COMPANY, AuditLogger.Ops.INSERT);
                companies.add(company);
            }
            
            tenantEnt = saveTenant(prodName, acctName, dset, companies);
            auditLogger.logEntity(tenantEnt, AuditLogger.Areas.TENANT, AuditLogger.Ops.INSERT);
        }
        
        TenantDTO tenant = populateTenantDTO(tenantEnt);
        return tenant;
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean deleteTenant(Long id) throws RecordNotFoundException {
        if (log.isDebugEnabled()) {
            log.debug("SERVICE invoked to delete Tenant with ID = {}", id);
        }
        
        Optional<Tenant> tenEntOpt = tenantRepo.findById(id);
        
        if (!tenEntOpt.isPresent()) {
            throw new RecordNotFoundException(
                    LogUtils.jsonize("", "id", id));
        }
        
        Tenant tenant = tenEntOpt.get();
        compDao.deleteCompByDset(tenant.getDataset());
        tenantRepo.deleteById(id);
        auditLogger.logEntity(tenant, AuditLogger.Areas.TENANT, AuditLogger.Ops.DELETE);
        
        return true;
    }

    /**
     *
     * @param ssoConfig
     * @return
     */
    public SSOConfigDTO createSSOConfig(SSOConfigDTO ssoConfig) {
        
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
        sSOConfiguration.setId(idGenerator.generate());
        sSOConfiguration.setIdpIssuer(ssoConfig.getIdpIssuer());
        sSOConfiguration.setIdpReqURL(ssoConfig.getIdpReqURL());
        sSOConfiguration.setNonSamlLogoutURL(ssoConfig.getNonSamlLogoutURL());
        sSOConfiguration.setRedirectToApplication(ssoConfig.getRedirectToApplication());
        sSOConfiguration.setSpConsumerURL(ssoConfig.getSpConsumerURL());
        sSOConfiguration.setSpIssuer(ssoConfig.getSpIssuer());
        
        Tenant tenant = getTenantByName(ssoConfig.getAcctName());
        sSOConfiguration.setTenant(tenant);

        //sSOConfiguration.setTenantSSOConf(tenantSSOConf);
        sSOConfiguration.setValidateIdpIssuer(ssoConfig.getValidateIdpIssuer());
        sSOConfiguration.setValidateRespSignature(ssoConfig.getValidateRespSignature());
        
        ssoConfigurationRepository.save(sSOConfiguration.getId(), sSOConfiguration);
        auditLogger.logEntity(sSOConfiguration, AuditLogger.Areas.SSO_CONF, AuditLogger.Ops.INSERT);
        return prepareConfig(sSOConfiguration);
    }

    /**
     *
     * @param ssoConfig
     * @return
     */
    public SSOConfigDTO updateSSOConfig(SSOConfigDTO ssoConfig) {
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
        
        Tenant tenant = getTenantByName(ssoConfig.getAcctName());
        sSOConfiguration.setTenant(tenant);

        //sSOConfiguration.setTenantSSOConf(tenantSSOConf);
        sSOConfiguration.setValidateIdpIssuer(ssoConfig.getValidateIdpIssuer());
        sSOConfiguration.setValidateRespSignature(ssoConfig.getValidateRespSignature());
        
        ssoConfigurationRepository.save(sSOConfiguration.getId(), sSOConfiguration);
        auditLogger.logEntity(sSOConfiguration, AuditLogger.Areas.SSO_CONF, AuditLogger.Ops.UPDATE);
        return prepareConfig(sSOConfiguration);
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean deleteSSOConfig(long id) {
        SSOConfiguration ssoConf = getSSOConfById(id);
        IgniteCache<Long, SSOConfiguration> cache = igniteInstance.cache(SSO_CONFIGURATION_CACHE);
        cache.query(new SqlFieldsQuery(JpaQueries.DELETE_SSO_CONFIG).setArgs(id));
        auditLogger.logEntity(ssoConf, AuditLogger.Areas.SSO_CONF, AuditLogger.Ops.DELETE);
        return true;
    }

    /**
     *
     * @param accountName
     * @param ssoConfigId
     * @param toUnlink
     * @return
     */
    public boolean linkSSOConfigToTenant(String accountName, long ssoConfigId, boolean toUnlink) {
        //TODO: Add implementation!
        return true;
    }

    /**
     *
     * @param accountName
     * @param ssoConfigId
     * @return
     */
    public boolean testSSOConfiguration(String accountName, long ssoConfigId) {
        //TODO: Add implementation!
        return true;
    }

    /**
     *
     * @param lastNoDays
     * @return
     */
    public List<AuditLogDTO> getAuditLogs(int lastNoDays) {
        List<AuditLogDTO> auditLogs = new ArrayList<>();
        IgniteCache<Long, AuditLog> auditLogCache = igniteInstance.cache(AUDIT_LOG_CACHE);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = now.minusDays(lastNoDays);
        SqlQuery sqlQry = new SqlQuery(AuditLog.class, "createdDate > ? ");
        try (QueryCursor<Cache.Entry<Long, AuditLog>> cursor = auditLogCache.query(sqlQry.setArgs(then.format(format)))) {
            for (Cache.Entry<Long, AuditLog> ag : cursor) {
                AuditLogDTO auditLog = new AuditLogDTO();
                auditLog.setCreatedDate(ag.getValue().getCreatedDate());
                auditLog.setAccount(ag.getValue().getAccountName());
                auditLog.setDataset(ag.getValue().getDatasetName());
                auditLog.setProduct(ag.getValue().getProductName());
                auditLog.setMessage(ag.getValue().getMessage());
                auditLogs.add(auditLog);
            }
        }
        return auditLogs;
    }

    /**
     * TODO: Add implementation!
     *
     * @param accountName
     * @return
     */
    public SSOConfigDTO getSSOConfigsByTenant(String accountName) {
        SSOConfigDTO config = new SSOConfigDTO();
        config.setAcctName("BSI");
        config.setId(1L);
        config.setDsplName("BSI SSO Config 1");
        return config;
    }

    /**
     * getSSOConfigs
     *
     * @return
     */
    public List<SSOConfigDTO> getSSOConfigs() {
        List<SSOConfigDTO> configs = new ArrayList<>();
        IgniteCache<Long, SSOConfiguration> ssoConfigCache = igniteInstance.cache(SSO_CONFIGURATION_CACHE);
        final String sql = "select * from SSOConfiguration";
        SqlQuery sqlQry = new SqlQuery(SSOConfiguration.class, sql);
        try (QueryCursor<Cache.Entry<Long, SSOConfiguration>> cursor = ssoConfigCache.query(sqlQry)) {
            for (Cache.Entry<Long, SSOConfiguration> cf : cursor) {
                SSOConfigDTO config = prepareConfig(cf.getValue());
                configs.add(config);
            }
        }
        return configs;
    }

    /**
     *
     * @param includeImported
     * @return
     */
    public List<TenantDTO> getTenants(boolean includeImported) {
        List<TenantDTO> tenants = new ArrayList<>();
        IgniteCache<Long, Tenant> tenantCache = igniteInstance.cache(TENANT_CACHE);
        SqlQuery sqlQry = new SqlQuery(Tenant.class, "imported= ?");
        try (QueryCursor<Cache.Entry<Long, Tenant>> cursor = tenantCache.query(sqlQry.setArgs(includeImported))) {
            for (Cache.Entry<Long, Tenant> tn : cursor) {
                TenantDTO tenant = new TenantDTO();
                Tenant tnt = tn.getValue();
                tenant.setId(tnt.getId());
                tenant.setAcctName(tnt.getAcctName());
                tenant.setDataset(tnt.getDataset());
                tenant.setProdName(tnt.getProdName());
                tenant.setEnabled(tnt.isEnabled());
                tenant.setImported(tnt.isImported());
                if (tnt.getTenantSSOConf() != null) {
                    tenant.setSsoConfId(tnt.getTenantSSOConf().getId());
                    tenant.setSsoConfDsplName(tnt.getTenantSSOConf().getSsoConfDsplName());
                }
                tenants.add(tenant);
            }
        }
        return tenants;
    }

    /**
     * @return
     */
    public SyncInfoDTO getLastSyncInfo() throws RecordNotFoundException {
        AdminMetadata adminMeta = adminMetaDao.get();
        
        if (adminMeta == null) {
            throw new RecordNotFoundException("Last Sync Info record is not found!");
        }
        
        SyncInfoDTO syncInfo = new SyncInfoDTO();
        syncInfo.setId(adminMeta.getId());
        syncInfo.setLastFullSync(adminMeta.getLastFullSync());
        syncInfo.setLastPerSync(adminMeta.getLastPerSync());
        syncInfo.setIsPerSyncOn(adminMeta.isIsPerSyncOn());
        syncInfo.setIsSyncInProgress(adminMeta.isIsSyncInProgress());
        return syncInfo;
    }

    /**
     * TODO: Add implementation!
     *
     * @return
     */
    public ProductDTO getProductsByTenants() {
        ProductDTO prod = new ProductDTO();
        prod.setAcctName("BSI");
        prod.setId(1L);
        prod.setProdName("TPF");
        return prod;
    }

    /**
     * TODO: Test-only stub! Add implementation!
     *
     * @param productsToRet
     */
    private Set<DatasetProductDTO> getTestProducts() throws Exception {
        String[][] dsetsProds = new String[][]{
            {"DSET1", "TPF", "ACCT1"},
            {"DSET2", "TF", "ACCT2"},
            {"DSET3", "CF", "ACCT3"}};
        return Arrays.asList(dsetsProds).stream().map(dp
                -> new DatasetProductDTO(1L, dp[2], dp[1], dp[0])).collect(Collectors.toSet());
    }

    /**
     * getTenantByName
     *
     * @param tenantName
     * @return
     */
    private Tenant getTenantByName(String tenantName) {
        IgniteCache<Long, Tenant> tenantCache = igniteInstance.cache(TENANT_CACHE);
        SqlQuery sqlQry = new SqlQuery(Tenant.class, "acctName= ?");
        Tenant tenant = null;
        try (QueryCursor<Cache.Entry<Long, Tenant>> cursor = tenantCache.query(sqlQry.setArgs(tenantName))) {
            for (Cache.Entry<Long, Tenant> tn : cursor) {
                tenant = tn.getValue();
            }
        }
        return tenant;
    }

    /**
     * getSSOConfById
     *
     * @param id
     * @return
     */
    private SSOConfiguration getSSOConfById(Long id) {
        IgniteCache<Long, SSOConfiguration> ssoConfCache = igniteInstance.cache(SSO_CONFIGURATION_CACHE);
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
     *
     * @param tenantEnt
     * @param acctName
     * @param companies
     * @return
     */
    private Tenant saveTenant(String prodName, String acctName,
            String dataset, Set<Company> companies) {
        Tenant tenantEnt = new Tenant();
        tenantEnt.setId(idGenerator.generate());
        tenantEnt.setAcctId(String.valueOf(idGenerator.generate()));
        tenantEnt.setAcctName(acctName);
        tenantEnt.setCompanies(companies);
        tenantEnt.setCreatedDate(LocalDateTime.now(ZoneOffset.UTC));
        tenantEnt.setDataset(dataset);
        tenantEnt.setEnabled(true);
        tenantEnt.setImported(false);
        tenantEnt.setLastModifiedDate(tenantEnt.getCreatedDate()
                .toInstant(ZoneOffset.UTC));
        tenantEnt.setProdId(String.valueOf(idGenerator.generate()));
        tenantEnt.setProdName(prodName);
        return tenantRepo.save(tenantEnt.getId(), tenantEnt);
    }

    /**
     *
     * @param dset
     * @param companyCID
     * @return
     */
    private Company saveCompany(String dset, String companyCID) {
        Company company = compDao.getCompByDsetCompCID(dset, companyCID);
        
        if (company != null) {
            return company;
        }
        
        company = new Company();
        company.setId(idGenerator.generate());
        company.setDataset(dset);
        company.setEnabled(true);
        company.setImported(false);
        company.setImportedDate(Instant.now(Clock.systemUTC()));
        company.setName(dset + " Internal Company");
        company.setSamlCid(companyCID);
        return compRepo.save(company.getId(), company);
    }

    /**
     *
     * @param tenantEnt
     * @return
     */
    private TenantDTO populateTenantDTO(Tenant ent) {
        TenantDTO dto = new TenantDTO();
        dto.setAcctId(ent.getAcctId());
        dto.setAcctName(ent.getAcctName());
        dto.setDataset(ent.getDataset());
        dto.setEnabled(ent.isEnabled());
        dto.setId(ent.getId());
        dto.setImported(ent.isImported());
        dto.setProdId(ent.getProdId());
        dto.setProdName(ent.getProdName());
        return dto;
    }
}
