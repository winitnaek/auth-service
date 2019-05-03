/**
 *
 */
package com.bsi.sec.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;

/**
 * Utility class to load default Spring profile!
 *
 * @author igorV
 *
 */
public final class DefaultProfileUtil {

    private static final String SPRING_PROFILE_DEFAULT = "spring.profiles.default";

    private DefaultProfileUtil() {
        super();
    }

    /**
     * When no Spring profile is specified the default one specified in this
     * method will be used.
     *
     * @param app
     */
    public static void addDefaultProfile(SpringApplication app) {
        Map<String, Object> dfltProps = new HashMap<>();
        dfltProps.put(SPRING_PROFILE_DEFAULT, com.bsi.sec.util.AppConstants.SPRING_PROFILE_DEV);
        app.setDefaultProperties(dfltProps);
    }
}
