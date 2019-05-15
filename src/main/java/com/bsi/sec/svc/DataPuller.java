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
