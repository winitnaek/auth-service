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
public interface DataSync {

    /**
     * Steps to run initial data sync the puller go in here.
     *
     * @throws Exception
     */
    public DataSyncResponse runInitialSync(LocalDateTime fromDateTime) throws Exception;

    /**
     * Steps to run periodic/incremental data sync 
     * the puller go in here.
     * 
     * @param fromDateTime
     * @return
     * @throws Exception 
     */
    public DataSyncResponse runPeriodicSync(LocalDateTime fromDateTime) throws Exception;

    /**
     * Any initialization logic should be specified here!
     * 
     * @throws Exception 
     */
    public void initializeSync() throws Exception;

    /**
     * Any post/resource cleanup steps go here!
     * 
     * @throws Exception 
     */    
    public void postSync() throws Exception;
}
