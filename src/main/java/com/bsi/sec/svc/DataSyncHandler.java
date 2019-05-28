/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.dao.AdminMetadataDao;
import com.bsi.sec.dao.TenantDao;
import com.bsi.sec.domain.AdminMetadata;
import com.bsi.sec.domain.Company;
import com.bsi.sec.domain.Tenant;
import com.bsi.sec.dto.DataSyncResponse;
import com.bsi.sec.exception.BadStateException;
import com.bsi.sec.exception.ProcessingException;
import com.bsi.sec.repository.AdminMetadataRepository;
import com.bsi.sec.repository.CompanyRepository;
import com.bsi.sec.repository.TenantRepository;
import com.bsi.sec.repository.TenantSSOConfRepository;
import static com.bsi.sec.util.AppConstants.BEAN_IGNITE_TX_MGR;
import static com.bsi.sec.util.CacheConstants.TENANT_CACHE;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import org.slf4j.Logger;
import java.util.List;
import javax.cache.Cache.Entry;
import java.util.TreeMap;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 *
 * Contains/runs data sync related steps.
 *
 * @author igorV
 */
@Component
@Transactional(transactionManager = BEAN_IGNITE_TX_MGR)
public class DataSyncHandler implements DataSyncResponseBuilder,
        DataSyncJobStateChecker {
    
    private final static Logger log = LoggerFactory.getLogger(DataSyncHandler.class);

    //Using this as limitation of TPF data availability in SF. SF#00130002
    private final static String BSI_eFormsFactory_SaaS_ID = "01tU0000000HOybIAG";
    private final static String BSI_eFormsFactory_SaaS = "BSI eFormsFactory SaaS";
    
    @Autowired
    private TenantRepository tenantRepo;
    
    @Autowired
    private TenantSSOConfRepository tenantSSOConfRepo;
    
    @Autowired
    private CompanyRepository companyRepo;
    
    @Autowired
    private AdminMetadataRepository adminMetaDataRepo;
    
    @Autowired
    private EntityIDGenerator idGenerator;
    
    @Autowired
    private SFDataPuller sfDataPuller;
    
    @Autowired
    private TPFDataPuller tpfDataPuller;
    
    @Autowired
    private AuditLogger auditLogger;
    
    @Autowired
    private TenantDao tenantDao;
    
    @Autowired
    private AdminMetadataDao adminMetaDataDao;
    
    @Autowired
    Ignite igniteInstance;

    /**
     * Responsible for performing initial data sync.
     *
     * @param tenants
     * @return
     */
    @Transactional
    public DataSyncResponse runInitialSync(LocalDateTime fromDtTm,
            boolean onDemandRequest)
            throws Exception {
        if (log.isInfoEnabled()) {
            log.info("Starting Initial Data Sync starting from {}.",
                    fromDtTm.toString());
        }
        
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        
        try {
            if (onDemandRequest) {
                checkIfSyncAlreadyRunning(adminMetaDataDao);
            }
            
            markAsInprogress();
            
            if (!onDemandRequest) {
                LocalDateTime lastInitSyncDateTime = getLastInitSyncDateTime();
                
                if (lastInitSyncDateTime != null) {
                    markAsDone();
                    return buildResponse(lastInitSyncDateTime, true);
                }
            }
            
            clearCustomDataFromCache();

            // Sync Tenants
            List<Tenant> tenants = getAllActiveTenants(fromDtTm);
            saveTenants(tenants, true);

            // Sync Companies
            List<Company> companies = getAllCompanies(fromDtTm);
            saveCompanies(companies, true);

            // Update Latest Sync Date in Metadata
            updateLastSyncDateTime(now, true);
            
            markAsDone();
            
            if (log.isInfoEnabled()) {
                log.info("Finished initial data sync.");
            }
            
            return buildResponse(now, true);
        } catch (Exception ex) {
            // Reset In-Prog indicator as Done in  case of exception!
            markAsDone();
            
            String errMsg = "Failed while running Initial Data Sync process!";
            
            if (log.isErrorEnabled()) {
                log.error(errMsg, ex);
            }
            
            throw new ProcessingException(errMsg, ex);
        }
    }

    /**
     * Responsible for performing periodic/incremental data sync.
     *
     * @param tenants
     * @return
     */
    @Transactional
    public DataSyncResponse runPeriodicSync(LocalDateTime fromDtTm,
            boolean onDemandRequest) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("Starting Periodic Data Sync process...");
        }
        
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        
        try {
            checkIfPerSyncIsEnabled();
            checkIfSyncAlreadyRunning(adminMetaDataDao);
            
            if (log.isInfoEnabled()) {
                log.info("Starting periodic data sync starting from {}.",
                        fromDtTm.toString());
            }
            
            markAsInprogress();

            // Sync Tenants
            List<Tenant> tenants = getUpdatedTenants(fromDtTm);
            saveTenants(tenants, false);

            // Sync Companies
            List<Company> companies = getUpdatedCompanies(fromDtTm);
            saveCompanies(companies, false);

            // Update Latest Sync Date in Metadata
            updateLastSyncDateTime(now, false);
            
            markAsDone();
            
            if (log.isInfoEnabled()) {
                log.info("Finished periodic data sync.");
            }
            
            return buildResponse(now, false);
        } catch (Exception ex) {
            // Reset In-Prog indicator as Done in  case of exception!
            markAsDone();
            
            String errMsg = "Failed while running Periodic Data Sync process!";
            
            if (log.isErrorEnabled()) {
                log.error(errMsg, ex);
            }
            
            throw new ProcessingException(errMsg, ex);
        }
    }

    /**
     * Retrieves last initial data sync date/time.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public LocalDateTime getLastInitSyncDateTime() {
        AdminMetadata admMeta = adminMetaDataDao.get();
        
        if (admMeta == null) {
            return null;
        }
        
        LocalDateTime lastSync = admMeta.getLastFullSync();
        return lastSync != null ? lastSync : null;
    }

    /**
     * Fetches all "SaaS Active" account records from Salesforce.
     *
     * @param fromDtTm
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Tenant> getAllActiveTenants(LocalDateTime fromDtTm)
            throws Exception {
        return sfDataPuller.pullAll(fromDtTm);
    }
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Tenant> getUpdatedTenants(LocalDateTime fromDtTm) throws Exception {
        return sfDataPuller.pullUpdates(fromDtTm);
    }

    /**
     * Fetches all TPF Companies from TPF DB.
     *
     * @param fromDtTm
     * @return
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Company> getAllCompanies(LocalDateTime fromDtTm) throws Exception {
        return tpfDataPuller.pullAll(fromDtTm);
    }

    /**
     * Fetches updated TPF Companies from TPF DB.
     *
     * @param fromDtTm
     * @return
     * @throws Exception
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<Company> getUpdatedCompanies(LocalDateTime fromDtTm) throws Exception {
        return tpfDataPuller.pullUpdates(fromDtTm);
    }

    /**
     * Update AdminMetadata.isSyncInProgress to FALSE
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsDone() {
        updateMetadataInprogFlag(false);
    }

    /**
     * Update AdminMetadata.isSyncInProgress to TRUE
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsInprogress() {
        updateMetadataInprogFlag(true);
    }

    /**
     *
     * @param adminMetaDao
     * @throws BadStateException
     */
    @Override
    public void checkIfPerSyncIsEnabled()
            throws BadStateException {
        AdminMetadata metaData = adminMetaDataDao.get();
        
        if (metaData != null && !metaData.isIsPerSyncOn()) {
            throw new BadStateException("Periodic Sync is turned off!");
        }
    }

    /**
     * Updates Metadata in-progress flag as specified by the parameter.
     *
     * @param isInprog
     */
    private void updateMetadataInprogFlag(boolean isInprog) {
        AdminMetadata ent = adminMetaDataDao.get();
        boolean metaDataExists = true;
        
        if (ent == null) {
            // Initially, create new record if not exists.
            ent = new AdminMetadata();
            ent.setId(idGenerator.generate());
            ent.setCreatedDate(LocalDateTime.now(ZoneOffset.UTC));
            metaDataExists = false;
        }
        
        ent.setIsSyncInProgress(isInprog);
        AdminMetadata savedEnt = adminMetaDataRepo.save(ent.getId(), ent);
        
        if (savedEnt != null) {
            if (log.isDebugEnabled()) {
                log.debug("Entity {} has been updated as Sync "
                        + (isInprog ? " In-Progress." : " Done."),
                        savedEnt.toString());
            }
            
            AuditLogger.Ops op = metaDataExists ? AuditLogger.Ops.UPDATE
                    : AuditLogger.Ops.INSERT;
            auditLogger.logEntity(savedEnt, AuditLogger.Areas.ADMIN_META_DATA,
                    op);
        } else {
            if (log.isErrorEnabled()) {
                log.error("Failed to mark entity {} as Sync"
                        + (isInprog ? " In-Progress." : " Done."), ent.toString());
            }
        }
    }

    /**
     *
     * @param tenants
     */
    private void saveTenants(List<Tenant> tenants, boolean isInit) {
        if (CollectionUtils.isEmpty(tenants)) {
            return;
        }
        
        if (log.isTraceEnabled()) {
            log.trace("Saving {} Tenants to store...", tenants.size());
        }
        
        tenants.forEach(t -> {
            if (log.isTraceEnabled()) {
                log.trace("\nSaving Tenant {}...", t.toString());
            }
            
            if (!isInit) {
                // Periodic Sync
                markExistingTenantForUpdate(t);
            }
            
            Tenant tenUpd = tenantRepo.save(t.getId(), t);
            
            if (log.isTraceEnabled()) {
                log.trace("Tenant {} is saved.\n", tenUpd.toString());
            }
        });
        
        if (isInit) {
            auditLogger.logAll(AuditLogger.Areas.TENANT, AuditLogger.Ops.INSERT);
        } else {
            auditLogger.logAll(AuditLogger.Areas.TENANT, AuditLogger.Ops.UPSERT);
        }
    }

    /**
     *
     * @param companies
     */
    private void saveCompanies(List<Company> companyList, boolean isInit) {
        if (CollectionUtils.isEmpty(companyList)) {
            return;
        }
        
        if (log.isDebugEnabled()) {
            log.debug("Saving {} Companies to store...", companyList.size());
        }
        
        List<Tenant> tenantList = new ArrayList<>();
        tenantList = getAllTenantsByProductId(BSI_eFormsFactory_SaaS_ID);
        log.debug(BSI_eFormsFactory_SaaS + " Tenants size before Company Save  : " + tenantList.size());
        
        List<Tenant> tenantSaveList = new ArrayList<>();
        List<Company> companySaveList = new ArrayList<>();
        
        for (Company company : companyList) {
            boolean companyTenantDsetAvailable = false;
            for (Tenant tenant : tenantList) {
                if (tenant.getDataset().equalsIgnoreCase(company.getDataset())) {
                    companyTenantDsetAvailable = true;
                    company.setTenant(tenant);
                    companySaveList.add(company);
                    break;
                }
            }
            if (!companyTenantDsetAvailable) {
                Tenant tn = new Tenant();
                tn.setAcctId(String.valueOf(tn.hashCode()));
                tn.setAcctName(company.getDataset() + "" + company.getSamlCid());
                tn.setDataset(company.getDataset());
                tn.setEnabled(true);
                tn.setImported(true);
                tn.setProdId(BSI_eFormsFactory_SaaS_ID);
                tn.setProdName(BSI_eFormsFactory_SaaS);
                tn.setId(idGenerator.generate());
                company.setTenant(tn);
                companySaveList.add(company);
                tenantSaveList.add(tn);
                companySaveList.add(company);
            }
        }
        if (tenantSaveList.size() > 0) {
            TreeMap<Long, Tenant> tenantsSave = new TreeMap<>();
            tenantSaveList.forEach((tn) -> {
                tenantsSave.put(tn.getId(), tn);
            });
            tenantRepo.save(tenantsSave);
        }
        TreeMap<Long, Company> companies = new TreeMap<>();
        if (companySaveList.size() > 0) {
            companySaveList.forEach((company) -> {
                companies.put(company.getId(), company);
            });
            companyRepo.save(companies);
        }
        log.debug("Tenant count after Company Save : " + tenantRepo.count());
        
        if (isInit) {
            auditLogger.logAll(AuditLogger.Areas.COMPANY,
                    AuditLogger.Ops.INSERT);
        } else {
            auditLogger.logAll(AuditLogger.Areas.COMPANY,
                    AuditLogger.Ops.UPSERT);
        }
        
        if (log.isDebugEnabled()) {
            log.debug("{} Companies are saved.\n", companies.size());
        }
    }

    /**
     * getAllTenantsByProductId
     *
     * @param prodId
     * @return
     */
    private List<Tenant> getAllTenantsByProductId(String prodId) {
        List<Tenant> tenantList = new ArrayList<>();
        IgniteCache<Long, Tenant> tenantCache = igniteInstance.cache(TENANT_CACHE);
        SqlQuery sqlQry = new SqlQuery(Tenant.class, "prodId= ?");
        try (QueryCursor<Entry<Long, Tenant>> cursor = tenantCache.query(sqlQry.setArgs(prodId))) {
            for (Entry<Long, Tenant> tn : cursor) {
                tenantList.add(tn.getValue());
            }
        }
        return tenantList;
    }

    /**
     * Clear all custom data from cache!
     */
    private void clearCustomDataFromCache() {
        if (log.isTraceEnabled()) {
            log.trace("Deleting all custom data from cache store...");
        }
        
        tenantSSOConfRepo.deleteAll();
        auditLogger.logAll(AuditLogger.Areas.TENANT_SSO_CONF,
                AuditLogger.Ops.DELETE);
        
        companyRepo.deleteAll();
        auditLogger.logAll(AuditLogger.Areas.COMPANY,
                AuditLogger.Ops.DELETE);
        
        tenantRepo.deleteAll();
        auditLogger.logAll(AuditLogger.Areas.TENANT,
                AuditLogger.Ops.DELETE);
        
        if (log.isTraceEnabled()) {
            log.trace("Done deleting all custom data from cache store!");
        }
    }

    /**
     * Updates "Last Full Sync Date/Time" field to the specified value.
     *
     * @param now
     */
    private void updateLastSyncDateTime(LocalDateTime dateTime, boolean fullSync) {
        AdminMetadata ent = adminMetaDataDao.get();
        
        if (ent == null) {
            // Initially, create new record if not exists.
            ent = new AdminMetadata();
            ent.setId(idGenerator.generate());
        }
        
        if (fullSync) {
            ent.setLastFullSync(dateTime);
        } else {
            ent.setLastPerSync(dateTime);
        }
        
        AdminMetadata savedEnt = adminMetaDataRepo.save(ent.getId(), ent);
        
        if (savedEnt != null) {
            if (log.isDebugEnabled()) {
                log.debug("Last " + (fullSync ? "Full" : "Periodic")
                        + " Sync Date/Time has been updated to {}",
                        dateTime.toString());
            }
            
            auditLogger.logEntity(savedEnt, AuditLogger.Areas.ADMIN_META_DATA,
                    AuditLogger.Ops.UPDATE);
            
        } else {
            if (log.isErrorEnabled()) {
                log.error("Failed to update Last " + (fullSync ? "Full" : "Periodic")
                        + " Sync Date/Time to {}",
                        dateTime.toString());
            }
        }
    }

    /**
     * <ul>
     * <li>Retrieves existing records from cache store by Account Name, Product
     * Name, Dataset Name</li>
     * <li>Updates incoming candidate record IDs with existing IDs</li>
     * </ul>
     *
     * @param tenantsIn
     */
    private void markExistingTenantForUpdate(Tenant tenIn) {
        if (log.isDebugEnabled()) {
            log.debug("As a part of Periodic Sync process, marking applicable"
                    + " Tenant records as existing records...");
        }
        
        String acctName = tenIn.getAcctName();
        String prodName = tenIn.getProdName();
        String dset = tenIn.getDataset();
        Tenant exstTenant = tenantDao.getTenantByDsetProdAcct(dset, prodName,
                acctName, false);
        
        if (exstTenant != null) {
            // Mark Tenant with existing ID!
            tenIn.setId(exstTenant.getId());
            
            if (log.isDebugEnabled()) {
                log.debug("Existing Tenant => {}", tenIn.toString());
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("New Tenant => {}", tenIn.toString());
            }
        }
    }
    
}
