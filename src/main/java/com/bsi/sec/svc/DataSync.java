/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 *
 * @author igorV
 */
public interface DataSync {

    /**
     * Steps to runInitialSync the puller go in here.
     *
     * @throws Exception
     */
    public DataSyncResponse runInitialSync(LocalDateTime fromDateTime) throws Exception;

    public DataSyncResponse runPeriodicSync(LocalDateTime fromDateTime) throws Exception;

    public void initializeSync() throws Exception;
}
