/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.domain.Tenant;
import com.bsi.sec.domain.AuditLog;
import com.bsi.sec.dto.AuditLogDTO;
import com.bsi.sec.dto.DatasetProductDTO;
import com.bsi.sec.dto.ProductDTO;
import com.bsi.sec.dto.SSOConfigDTO;
import com.bsi.sec.dto.SyncInfoDTO;
import com.bsi.sec.dto.TenantDTO;
import static com.bsi.sec.util.CacheConstants.TENANT_CACHE;
import static com.bsi.sec.util.CacheConstants.AUDIT_LOG_CACHE;
import com.bsi.sec.util.DateUtils;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.cache.Cache;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for serving REST resource/controller requests!
 *
 * //TODO: Add validation, exception handling, comments.
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

        boolean isSFSyncEnabled = true;
        return isSFSyncEnabled;
    }

    /**
     *
     * @param accountName
     * @param productName
     * @param datasetName
     * @return
     */
    public TenantDTO createTenant(String accountName, String productName,
            String datasetName) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("SERVICE invoked to add new Tenant with Account Name = {}, "
                    + "Product Name = {}, Dataset Name = {}", accountName,
                    productName, datasetName);
        }

        TenantDTO tenant = new TenantDTO(1L, "BSI Inc.",
                "TPF", "BSI_DSET_1");
        tenant.setEnabled(true);
        tenant.setImported(true);
        tenant.setSsoConfId(1L);
        tenant.setSsoConfDsplName("SSO Conf 1");
        return tenant;
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean deleteTenant(Long id) {
        if (log.isDebugEnabled()) {
            log.debug("SERVICE invoked to delete Tenant with ID = {}", id);
        }

        boolean isDeleted = true;
        return isDeleted;
    }

    /**
     *
     * @param ssoConfig
     * @return
     */
    public SSOConfigDTO createSSOConfig(SSOConfigDTO ssoConfig) {
        SSOConfigDTO config = new SSOConfigDTO();
        config.setAcctName("BSI");
        config.setDsplName("BSI SSO Config");
        config.setEnabled(true);
        return config;
    }

    /**
     *
     * @param ssoConfig
     * @return
     */
    public SSOConfigDTO updateSSOConfig(SSOConfigDTO ssoConfig) {
        SSOConfigDTO config = new SSOConfigDTO();
        config.setAcctName("BSI");
        config.setDsplName("BSI SSO Config 2");
        config.setEnabled(true);
        return config;
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean deleteSSOConfig(long id) {
        //TODO: Add implementation!
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
        IgniteCache<Long, AuditLog> tenantCache = igniteInstance.cache(AUDIT_LOG_CACHE);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime then = now.minusDays(lastNoDays);
        SqlQuery sqlQry = new SqlQuery(AuditLog.class, "createdDate > ? ");
        try (QueryCursor<Cache.Entry<Long, AuditLog>> cursor = tenantCache.query(sqlQry.setArgs(then.format(format)))) {
            for (Cache.Entry<Long, AuditLog> ag : cursor){
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
     * TODO: Add implementation!
     *
     * @param accountName
     * @return
     */
    public List<SSOConfigDTO> getSSOConfigs() {
        List<SSOConfigDTO> configs = new ArrayList<>();
        SSOConfigDTO config = new SSOConfigDTO();
        config.setAcctName("BSI");
        config.setId(1L);
        config.setDsplName("BSI SSO Config 1");
        configs.add(config);

        SSOConfigDTO config1 = new SSOConfigDTO();
        config1.setAcctName("Walmart");
        config1.setId(2L);
        config1.setDsplName("Walmart SSO Config 1");
        configs.add(config1);
        return configs;
    }

    /**
     * TODO: Add implementation!
     *
     * @param includeImported
     * @return
     */
    public List<TenantDTO> getTenants(boolean includeImported) {
        List<TenantDTO> tenants = new ArrayList<>();
        IgniteCache<Long, Tenant> tenantCache = igniteInstance.cache(TENANT_CACHE);
        SqlQuery sqlQry = new SqlQuery(Tenant.class, "imported= ?");
        try (QueryCursor<Cache.Entry<Long, Tenant>> cursor = tenantCache.query(sqlQry.setArgs(includeImported))) {
            for (Cache.Entry<Long, Tenant> tn : cursor){
                TenantDTO tenant = new TenantDTO();
                Tenant tnt = tn.getValue();
                tenant.setId(tnt.getId());
                tenant.setAcctName(tnt.getAcctName());
                tenant.setDataset(tnt.getDataset());
                tenant.setProdName(tnt.getProdName());
                tenant.setEnabled(tnt.isEnabled());
                tenant.setImported(tnt.isImported());
                if(tnt.getTenantSSOConf() !=null){
                    tenant.setSsoConfId(tnt.getTenantSSOConf().getId());
                    tenant.setSsoConfDsplName(tnt.getTenantSSOConf().getSsoConfDsplName());
                }
                tenants.add(tenant);
            }
        }
        return tenants;
    }

    /**
     * TODO: Add implementation!
     *
     * @return
     */
    public SyncInfoDTO getLastSyncInfo() {
        SyncInfoDTO syncInfo = new SyncInfoDTO();
        syncInfo.setId(1L);
        LocalDateTime lastSync = LocalDateTime.now(ZoneOffset.UTC);
        syncInfo.setLastFullSync(lastSync);
        syncInfo.setLastPerSync(lastSync);
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

}
