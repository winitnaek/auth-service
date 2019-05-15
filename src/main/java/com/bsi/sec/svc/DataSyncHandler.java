/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.domain.AdminMetadata;
import com.bsi.sec.domain.Tenant;
import com.bsi.sec.dto.DataSyncResponse;
import com.bsi.sec.repository.AdminMetadataRepository;
import com.bsi.sec.repository.CompanyRepository;
import com.bsi.sec.repository.TenantRepository;
import com.bsi.sec.repository.TenantSSOConfRepository;
import java.util.Iterator;
import org.slf4j.Logger;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * Contains logic to update cache store.
 *
 * @author igorV
 */
@Component
@Transactional
public class DataSyncHandler {

    private final static Logger log = LoggerFactory.getLogger(DataSyncHandler.class);

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

    /**
     * Responsible for syncing tenant information against the cache store.
     *
     * @param allTenants
     * @return
     */
    @Transactional
    public DataSyncResponse syncTenantData(List<Tenant> allTenants) {
        if (log.isInfoEnabled()) {
            log.info("Starting syncing {} Tenants", allTenants.size());
        }

        clearCustomDataFromCache();
        saveTenants(allTenants);
        DataSyncResponse response = new DataSyncResponse();
        return response;
    }

    /**
     * Update AdminMetadata.isSyncInProgress to FALSE
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsSyncDone() {
        updateMetadataInprogFlag(false);
    }

    /**
     * Update AdminMetadata.isSyncInProgress to TRUE
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsSyncInprogress() {
        updateMetadataInprogFlag(true);
    }

    /**
     * Updates Metadata in-progress flag as specified by the parameter.
     *
     * @param isInprog
     */
    private void updateMetadataInprogFlag(boolean isInprog) {
        Iterator<AdminMetadata> metaDataEntIter
                = adminMetaDataRepo.findAll().iterator();
        AdminMetadata ent;

        if (metaDataEntIter.hasNext()) {
            ent = metaDataEntIter.next();
        } else {
            ent = new AdminMetadata();
            ent.setId(idGenerator.generate(ent));
        }

        ent.setIsSyncInProgress(isInprog);
        AdminMetadata savedEnt = adminMetaDataRepo.save(ent.getId(), ent);

        if (savedEnt != null) {
            if (log.isDebugEnabled()) {
                log.debug("Entity {} has been updated as Sync "
                        + (isInprog ? " In-Progress." : " Done."),
                        savedEnt.toString());
            }
        } else {
            if (log.isErrorEnabled()) {
                log.error("Failed to mark entity {} as Sync"
                        + (isInprog ? " In-Progress." : " Done."), ent.toString());
            }
        }
    }

    /**
     *
     * @param allTenants
     */
    private void saveTenants(List<Tenant> allTenants) {
        if (log.isTraceEnabled()) {
            log.trace("Saving {} Tenants to store...", allTenants.size());
        }

        allTenants.forEach(t -> {
            if (log.isTraceEnabled()) {
                log.trace("Saving Tenant {}...", t.toString());
            }

            Tenant tenUpd = tenantRepo.save(t.getId(), t);

            if (log.isTraceEnabled()) {
                log.trace("Tenant {} is saved.", tenUpd.toString());
            }
        });
    }

    /**
     * Clear all custom data from cache!
     */
    private void clearCustomDataFromCache() {
        if (log.isTraceEnabled()) {
            log.trace("Deleting all custom data from cache store...");
        }

        tenantSSOConfRepo.deleteAll();
        companyRepo.deleteAll();
        tenantRepo.deleteAll();

        if (log.isTraceEnabled()) {
            log.trace("Done deleting all custom data from cache store!");
        }
    }

}
