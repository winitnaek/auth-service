/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.config;

import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author igorV
 */
public interface UserProvider {

    public final static String SERVICE_USER = "SERVICE";

    /**
     *
     * @return
     */
    public default String getUser() {
        try {
            return (String) SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal();
        } catch (Exception e) {
            return SERVICE_USER;
        }
    }
}
