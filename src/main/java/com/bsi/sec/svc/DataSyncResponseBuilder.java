/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.dto.DataSyncResponse;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Sync Job response builder.</p>
 *
 * @author igorV
 */
public interface DataSyncResponseBuilder {

    final static Logger log = LoggerFactory.getLogger(DataSyncResponseBuilder.class);

    /**
     * Build job response.
     *
     * @param lastInitSyncDateTime
     * @return
     */
    public default DataSyncResponse buildResponse(LocalDateTime lastInitSyncDateTime,
            boolean isFullSync) {
        DataSyncResponse response = new DataSyncResponse();
        response.setLastRunDateTime(lastInitSyncDateTime);
        response.setIsSucessfull(true);

        String msg = "Last " + (isFullSync ? "Full" : "Periodic") + " SF Data"
                + " Sync request submitted at " + lastInitSyncDateTime;
        response.setMessage(msg);

        if (log.isInfoEnabled()) {
            log.info(response.toString());
        }

        return response;
    }
}
