/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.dto.DataSyncResponse;

/**
 *
 * @author igorV
 */
public interface DataSyncJob {

    /**
     * Contract for running the data sync job.
     *
     * @return
     * @throws Exception
     */
    public DataSyncResponse run() throws Exception;
}
