/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.dto.DataSyncResponse;
import java.time.LocalDateTime;

/**
 *
 * @author igorV
 */
public interface AsyncDataSyncJob {

    /**
     * Contract for running the data sync job.
     *
     * @return
     * @throws Exception
     */
    public DataSyncResponse run(LocalDateTime fromDtTm, boolean onDemandRequest) throws Exception;

}
