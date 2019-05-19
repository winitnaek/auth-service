/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.dto.DataSyncResponse;
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

/**
 * Asynchronous Initial Data Sync job.
 *
 * @author igorV
 */
@Component
public class AsyncInitialDataSyncJob implements AsyncDataSyncJob, DataSyncResponseBuilder {

    private final static Logger log = LoggerFactory.getLogger(AsyncInitialDataSyncJob.class);

    @Autowired
    private Ignite ignite;

    @Autowired
    private DataSyncHandler dataSyncHandler;

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
        asyncCompute.broadcast(() -> dataSyncHandler.runInitialSync(fromDtTm, onDemandRequest));
        ComputeTaskFuture<ArrayList<DataSyncResponse>> fut = asyncCompute.future();
        fut.listen(f -> {
            DataSyncResponse response = f.get().get(0);

            if (response != null) {
                if (log.isInfoEnabled()) {
                    log.info("Full Data Sync job with From Date/Time {} "
                            + "completed! {}", fromDtTm,
                            LogUtils.jsonize("Full Data Sync", "response", response));
                }
            } else {
                String errMsg = "Failed while running Full Data Sync job!";

                if (log.isErrorEnabled()) {
                    log.error(errMsg);
                }
            }
        });

        return buildResponse(LocalDateTime.now(), true);
    }
}
