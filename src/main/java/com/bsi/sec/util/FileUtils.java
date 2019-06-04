/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.util;

import com.bsi.sec.exception.ConfigurationException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author igorV
 */
public class FileUtils {
    
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);
    
    public static Optional<Path> createDirectoryIfNotExists(String pathIn) throws ConfigurationException {
        try {
            Path path = Paths.get(pathIn);

            if (!Files.exists(path)) {
                Path dir = Files.createDirectory(path);
                if (log.isInfoEnabled()) {
                    log.info("Created directory! --> " + dir.toString());
                }
                return Optional.of(dir);
            }
            if (log.isInfoEnabled()) {
                log.info("Directory already exists! --> {}", pathIn);
            }
            return Optional.empty();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(LogUtils.jsonize(
                        "msg", "Failed to create temp directory {}. "
                        + ExceptionUtils.getRootCauseMessage(e),
                        "Directory", pathIn));
            }
            throw new ConfigurationException(Errors.FAILED_TO_CREATE_TEMPDIR, pathIn);
        }
    }
    
}
