/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.helper;

import com.bsi.sec.config.SecurityServiceProperties;
import com.bsi.sec.svc.DataSyncHandler;
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
    private SecurityServiceProperties props;

    @Autowired
    private SFDataPuller sfDataPuller;

    @Autowired
    private DataSyncHandler dataSyncHandler;

    public void initialize() throws Exception {
        runInitialDataSync();
    }

    @Override
    public void close() throws IOException {
        Ignition.stopAll(true);

        try {
            sfDataPuller.postCleanup();
        } catch (Exception ex) {
            if (log.isErrorEnabled()) {
                log.error("Failed to perform cleanup steps!", ex);
            }
        }
    }

    private void runInitialDataSync() throws Exception {
        sfDataPuller.initialize();
        dataSyncHandler.runInitialSync(DateUtils.defaultFromSyncTime());
    }
}
