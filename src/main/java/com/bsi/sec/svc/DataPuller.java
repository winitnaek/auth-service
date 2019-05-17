/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author igorV
 */
public interface DataPuller {

    /**
     * Retrieves all data (e.g. for initial data sync)
     *
     * @param fromDtTm
     * @return
     * @throws Exception
     */
    public List<?> pullAll(LocalDateTime fromDtTm) throws Exception;

    /**
     * Retrieves changed data (e.g. for periodic data sync)
     *
     * @param fromDtTm
     * @return
     * @throws Exception
     */
    public List<?> pullUpdates(LocalDateTime fromDtTm) throws Exception;

    /**
     * Any initialization logic should be specified here!
     *
     * @throws Exception
     */
    public void initialize() throws Exception;

    /**
     * Any post/resource cleanup steps go here!
     *
     * @throws Exception
     */
    public void postCleanup() throws Exception;
}
