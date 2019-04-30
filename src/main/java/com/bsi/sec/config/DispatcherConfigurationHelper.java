/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.config;

import javax.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;

/**
 * Helper to configure dispatchers for spring mvc and cxf.
 *
 * @author SudhirP
 */
public final class DispatcherConfigurationHelper {

    private static final Logger log = LoggerFactory.getLogger(DispatcherConfigurationHelper.class);

    public static void onStartupAfterContextCreation(ServletContext sctx, WebApplicationContext ctx) {
    }

}
