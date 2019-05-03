/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.config;

import com.bsi.sec.util.DefaultProfileUtil;
import java.rmi.UnknownHostException;
import org.hibernate.internal.util.config.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

/**
 * Test context. Used only for testing.
 *
 * @author sudhirp
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class})
@EnableConfigurationProperties({SecurityServiceProperties.class})
@ComponentScan(basePackages = {"com.bsi.sec.config"})
public class TestApplicationContext {

    private static final Logger log = LoggerFactory
            .getLogger(TestApplicationContext.class);

    public static void main(String[] args) throws UnknownHostException, ConfigurationException {
        SpringApplication app = new SpringApplication(
                TestApplicationContext.class);
        DefaultProfileUtil.addDefaultProfile(app);
        Environment env = app.run(args).getEnvironment();
    }
}
