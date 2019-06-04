/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.dao.AdminMetadataDao;
import com.bsi.sec.domain.AdminMetadata;
import com.bsi.sec.dto.DataSyncResponse;
import com.bsi.sec.exception.RecordNotFoundException;
import static com.bsi.sec.util.AppConstants.SYNC_EVERY_MINS;
import com.bsi.sec.util.LogUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.compute.ComputeTaskFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Asynchronous Initial Data Sync job.
 *
 * @author igorV
 */
@Component
public class AsyncPeriodicDataSyncJob implements AsyncDataSyncJob, DataSyncResponseBuilder {

    private final static Logger log = LoggerFactory.getLogger(AsyncPeriodicDataSyncJob.class);

    @Autowired
    private Ignite ignite;

    @Autowired
    private DataSyncHandler dataSyncHandler;

    @Autowired
    private AdminMetadataDao adminMetaDataDao;

    private IgniteCompute asyncCompute;

    @PostConstruct
    public void initialize() {
        asyncCompute = ignite.compute().withAsync();
    }

    /**
     *
     *
     * @return @throws Exception
     */
    @Override
    public DataSyncResponse run(LocalDateTime fromDtTm, boolean onDemandRequest) throws Exception {
        asyncCompute.broadcast(() -> dataSyncHandler.runPeriodicSync(fromDtTm, onDemandRequest));
        ComputeTaskFuture<ArrayList<DataSyncResponse>> fut = asyncCompute.future();
        fut.listen(f -> {
            DataSyncResponse response = f.get().get(0);

            if (response != null) {
                if (log.isInfoEnabled()) {
                    log.info("Periodic Data Sync job with From Date/Time {} "
                            + "completed! {}", fromDtTm,
                            LogUtils.jsonize("response", response));
                }
            } else {
                String errMsg = "Failed while running Periodic Data Sync job!";

                if (log.isErrorEnabled()) {
                    log.error(errMsg);
                }
            }
        });

        return buildResponse(LocalDateTime.now(), false);
    }

    /**
     * Shortcut version of Periodic Sync job.
     *
     */
    public DataSyncResponse run() throws Exception {
        LocalDateTime fromDtTm = getLastPerSyncDateTime();

        if (fromDtTm == null) {
            throw new RecordNotFoundException("Admin Metadata record is not found!");
        }

        return run(fromDtTm, false);
    }

    /**
     * Retrieves last initial data sync date/time.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public LocalDateTime getLastPerSyncDateTime() {
        AdminMetadata admMeta = adminMetaDataDao.get();

        if (admMeta == null) {
            return null;
        }

        LocalDateTime lastSync = admMeta.getLastPerSync();

        if (lastSync == null) {
            lastSync = LocalDateTime.now().minusMinutes(SYNC_EVERY_MINS);
        }

        return lastSync;
    }
}
