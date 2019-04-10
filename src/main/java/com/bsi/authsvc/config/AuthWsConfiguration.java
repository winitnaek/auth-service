/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.authsvc.config;

import com.bsi.authsvc.exception.ConfigurationException;
import com.bsi.authsvc.util.AppConstants;
import com.bsi.authsvc.util.FileUtils;
import com.bsi.authsvc.util.LogUtils;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 * @author igorV
 */
@Configuration
@PropertySource("classpath:/authws.properties")
@ComponentScan(basePackages = {
    "com.bsi.authsvc.exception", "com.bsi.authsvc.config"
}
)
public class AuthWsConfiguration {

    private final static Logger log = LoggerFactory.getLogger(AuthWsConfiguration.class);

    @Autowired
    private Environment env;

    @Autowired
    private AuthWSProperties props;

    @Bean
    public String createReqDirectories() throws ConfigurationException {
        String tempdir = props.getDirectories().getTempDir();
        createDirectory(tempdir);
        return String.valueOf(AppConstants.SUCCESS);
    }

    private void createDirectory(String dir) throws com.bsi.authsvc.exception.ConfigurationException {
        if (Files.notExists(FileSystems.getDefault().getPath(dir))) {
            if (log.isWarnEnabled()) {
                log.warn(LogUtils.jsonize("Missing directory", "dir", dir));
            }

            Optional<Path> path = FileUtils.createDirectoryIfNotExists(dir);

            if (path.isPresent()) {
                String newdir = path.get().toString();

                if (log.isInfoEnabled()) {
                    log.info(LogUtils.jsonize("Created directory", "new directory", newdir));
                }
            }
        }
    }
}
