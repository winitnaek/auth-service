/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.config;

import com.bsi.sec.domain.SSOConfiguration;
import com.bsi.sec.domain.Tenant;
import com.bsi.sec.domain.TenantSSOConf;
import java.sql.SQLException;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 * @author igorV
 */
@Configuration
@EnableIgniteRepositories
public class StoreConfiguration implements WebMvcConfigurer {

    private final static Logger log = LoggerFactory.getLogger(StoreConfiguration.class);

    private static final String[] JPA_ENTITIES_PACKAGES = new String[]{"com.bsi.sec.domain"};

    @Autowired
    private SecurityServiceProperties props;

    @Bean
    @Primary
    @SuppressWarnings("rawtypes")
    public Ignite igniteInstance() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        // Setting some custom name for the node.
        cfg.setIgniteInstanceName("tenantDataNode");
        // Enabling peer-class loading feature.
        cfg.setPeerClassLoadingEnabled(true);

        // Defining and creating a new cache to be used by Ignite Spring Data
        // repository.
        CacheConfiguration<Long, Tenant> ccfgTenant = new CacheConfiguration<>("TenantCache");
        CacheConfiguration<Long, SSOConfiguration> ccfgSSOConf = new CacheConfiguration<>("SSOConfCache");
        CacheConfiguration<Long, TenantSSOConf> ccfgTenantSSOConf = new CacheConfiguration<>("TenantSSOConfCache");
        // Setting SQL schema for the cache.
        ccfgSSOConf.setIndexedTypes(Long.class, SSOConfiguration.class);
        ccfgTenant.setIndexedTypes(Long.class, Tenant.class);
        ccfgTenantSSOConf.setIndexedTypes(Long.class, TenantSSOConf.class);

        cfg.setCacheConfiguration(new CacheConfiguration[]{ccfgTenantSSOConf, ccfgTenant, ccfgSSOConf});

        return Ignition.start(cfg);
    }

    @Bean
    @Primary
    public EntityManagerFactory entityManagerFactory() throws SQLException {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPackagesToScan(JPA_ENTITIES_PACKAGES);
        JpaDialect jpaDialect = new HibernateJpaDialect();
        factory.setJpaDialect(jpaDialect);
        //
        Properties jpaProps = new Properties();
        jpaProps.put("hibernate.dialect", props.getJpa().getDialect());
        jpaProps.put("hibernate.hbm2ddl.auto", "");
        jpaProps.put("hibernate.archive.autodetection", "");
        factory.setJpaProperties(jpaProps);
        log.info("JPA Property Map -> " + factory.getJpaPropertyMap().toString());
        //        
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setDataSource(dataSource());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    @Primary
    public PlatformTransactionManager transactionManager() throws SQLException {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    @Bean
    @Primary
    public DataSource dataSource() throws SQLException {
        return createDataSource();
    }

    private DataSource createDataSource() throws SQLException {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(props.getDataSource().getUrl());
        ds.setDriverClassName(props.getDataSource().getDriverClassName());
        return ds;
    }

}
