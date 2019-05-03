/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

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
    public void runInitialSync() throws Exception;

    public void runPeriodicSync() throws Exception;

    public void initializeSync() throws Exception;
}
