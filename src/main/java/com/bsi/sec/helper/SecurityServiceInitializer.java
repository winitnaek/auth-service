/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.helper;

import com.bsi.sec.svc.AsyncInitialDataSyncJob;
import com.bsi.sec.svc.PeriodicDataSyncJobScheduler;
import com.bsi.sec.svc.SFDataPuller;
import com.bsi.sec.util.DateUtils;
import java.io.Closeable;
import java.io.IOException;
import org.apache.ignite.Ignition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author igorV
 */
@Component
public class SecurityServiceInitializer implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(SecurityServiceInitializer.class);

    @Autowired
    private SFDataPuller sfDataPuller;

    @Autowired
    private AsyncInitialDataSyncJob initDataSyncJob;

    @Autowired
    private PeriodicDataSyncJobScheduler scheduler;

    /**
     *
     * @throws Exception
     */
    public void initialize() throws Exception {
        runInitialDataSync();
        schedulePeriodicSyncs();
    }

    /**
     *
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Resource close() triggered!");
        }

        try {
            Ignition.stopAll(true);
            sfDataPuller.postCleanup();
        } catch (Exception ex) {
            if (log.isErrorEnabled()) {
                log.error("Failed to perform cleanup steps!", ex);
            }
        }
    }

    /**
     *
     * @throws Exception
     */
    private void runInitialDataSync() throws Exception {
        sfDataPuller.initialize();
        initDataSyncJob.run(DateUtils.defaultFromSyncTime(), false);
    }

    /**
     *
     * @throws Exception
     */
    private void schedulePeriodicSyncs() throws Exception {
        scheduler.schedule();
    }

}
