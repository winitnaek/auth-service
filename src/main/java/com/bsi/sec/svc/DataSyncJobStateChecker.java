/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.dao.AdminMetadataDao;
import com.bsi.sec.domain.AdminMetadata;
import com.bsi.sec.exception.AnotherSyncRunningException;
import com.bsi.sec.exception.BadStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author igorV
 */
public interface DataSyncJobStateChecker {

    public final static Logger log = LoggerFactory.getLogger(DataSyncJobStateChecker.class);

    /**
     * <pre>
     * Checks if another sync process is in progress. If one is found,
     * then <code>AnotherSyncRunningException</code> is thrown.
     * </pre>
     *
     * @param adminMetaDao
     * @throws AnotherSyncRunningException
     */
    public default void checkIfSyncAlreadyRunning(AdminMetadataDao adminMetaDao)
            throws AnotherSyncRunningException {
        if (log.isDebugEnabled()) {
            log.debug("Checking if another Sync Job already running..");
        }

        AdminMetadata metaData = adminMetaDao.get();

        if (metaData != null && metaData.isIsSyncInProgress()) {
            throw new AnotherSyncRunningException();
        }
    }

    /**
     *
     *
     * @param adminMetaDao
     * @throws PeriodicSyncDIsabledException
     */
    public void checkIfPerSyncIsEnabled()
            throws BadStateException;
}
