/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.helper;

import com.bsi.sec.config.SecurityServiceProperties;
import java.io.Closeable;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author igorV
 */
@Component
public class SecurityWSInitializer implements Closeable {

    @Autowired
    private SecurityServiceProperties props;

    private static final Logger logger = LoggerFactory.getLogger(SecurityWSInitializer.class);

    public void initialize() {

        logger.warn("Please implement any startup services such as quartz timers etc. for " + props.getAppName());
    }

    @Override
    public void close() throws IOException {
        logger.warn("Please implement any cleanup code, such as shutting down timers etc.");
    }
}
