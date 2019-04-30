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
public interface DataPuller {

    /**
     * Steps to initialize the puller go in here.
     *
     * @throws Exception
     */
    public void initialize() throws Exception;

    public void initialPull() throws Exception;

    public void periodicPull() throws Exception;
}
