/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.config;

import com.bsi.sec.domain.AdminMetadata;
import com.bsi.sec.domain.AuditLog;
import com.bsi.sec.domain.Company;
import com.bsi.sec.domain.SSOConfiguration;
import com.bsi.sec.domain.Tenant;
import com.bsi.sec.domain.TenantSSOConf;
import com.bsi.sec.exception.ConfigurationException;
import static com.bsi.sec.util.AppConstants.BEAN_IGNITE_TX_MGR;
import static com.bsi.sec.util.CacheConstants.ADMIN_METADATA_CACHE;
import static com.bsi.sec.util.CacheConstants.AUDIT_LOG_CACHE;
import static com.bsi.sec.util.CacheConstants.COMPANY_CACHE;
import static com.bsi.sec.util.CacheConstants.SEC_CACHE;
import static com.bsi.sec.util.CacheConstants.SEC_CACHE_MGR_BEAN;
import static com.bsi.sec.util.CacheConstants.SSO_CONFIGURATION_CACHE;
import static com.bsi.sec.util.CacheConstants.TENANT_CACHE;
import static com.bsi.sec.util.CacheConstants.TENANT_SSO_CONF_CACHE;
import org.apache.ignite.IgniteCheckedException;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.logger.log4j2.Log4J2Logger;
import org.apache.ignite.springdata20.repository.config.EnableIgniteRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import static com.bsi.sec.util.CacheConstants.SEC_SVC_DATA_NODE;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import static org.apache.ignite.cache.CacheAtomicityMode.TRANSACTIONAL;
import org.apache.ignite.cache.spring.SpringCacheManager;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.transactions.spring.SpringTransactionManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Primary;

/**
 *
 * @author igorV
 */
@Configuration
@EnableIgniteRepositories(basePackages = {"com.bsi.sec.repository"})
@EnableCaching
public class StoreConfiguration implements WebMvcConfigurer, CachingConfigurer {

    private final static Logger log = LoggerFactory.getLogger(StoreConfiguration.class);

    @Bean
    @Primary
    public Ignite igniteInstance() {
        Ignite ignite = Ignition.start(igniteCfg());
        ignite.cluster().active(true);
        return ignite;
    }

    @Bean(name = BEAN_IGNITE_TX_MGR)
    public SpringTransactionManager igniteSpringTxMgr() {
        SpringTransactionManager mgr = new SpringTransactionManager();
        mgr.setIgniteInstanceName(SEC_SVC_DATA_NODE);
        return mgr;
    }

    @Bean
    public Log4J2Logger igniteLog4J2Logger() throws ConfigurationException {
        Log4J2Logger logger;
        try {
            logger = new Log4J2Logger(StoreConfiguration.class.getClassLoader()
                    .getResource("log4j2.xml"));
            return logger;
        } catch (IgniteCheckedException ex) {
            String err = "Failed to create Ignite Log4j2Logger bean!";
            if (log.isErrorEnabled()) {
                log.error(err, ex);
            }
            throw new ConfigurationException(err, ex);
        }

    }

    /**
     * Builds Ignite configuration.
     *
     * @return
     */
    @Bean
    public IgniteConfiguration igniteCfg() {
        IgniteConfiguration cfg = new IgniteConfiguration();
        // Setting some custom name for the node.
        cfg.setIgniteInstanceName(SEC_SVC_DATA_NODE);
        // Enabling peer-class loading feature.
        cfg.setPeerClassLoadingEnabled(true);

        // Defining and creating a new cache to be used by Ignite Spring Data
        // repository.
        CacheConfiguration<Long, AdminMetadata> ccfgMetadta = new CacheConfiguration<>(ADMIN_METADATA_CACHE);
        CacheConfiguration<Long, AuditLog> ccfgAuditLog = new CacheConfiguration<>(AUDIT_LOG_CACHE);
        CacheConfiguration<Long, Company> ccfgCompany = new CacheConfiguration<>(COMPANY_CACHE);
        CacheConfiguration<Long, Tenant> ccfgTenant = new CacheConfiguration<>(TENANT_CACHE);
        CacheConfiguration<Long, SSOConfiguration> ccfgSSOConf = new CacheConfiguration<>(SSO_CONFIGURATION_CACHE);
        CacheConfiguration<Long, TenantSSOConf> ccfgTenantSSOConf = new CacheConfiguration<>(TENANT_SSO_CONF_CACHE);
        CacheConfiguration<Long, String> ccfgSecConf = new CacheConfiguration<>(SEC_CACHE);
        // Setting SQL schema for the cache.
        ccfgMetadta
                .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
                .setIndexedTypes(Long.class, AdminMetadata.class);
        ccfgAuditLog
                .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
                .setIndexedTypes(Long.class, AuditLog.class);
        ccfgCompany
                .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
                .setIndexedTypes(Long.class, Company.class);
        ccfgSSOConf
                .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
                .setIndexedTypes(Long.class, SSOConfiguration.class);
        ccfgTenant
                .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
                .setIndexedTypes(Long.class, Tenant.class);
        ccfgTenantSSOConf
                .setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL)
                .setIndexedTypes(Long.class, TenantSSOConf.class);
        ccfgSecConf
                .setAtomicityMode(TRANSACTIONAL)
                .setIndexedTypes(Long.class, String.class)
                .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_MINUTE))
                .setEagerTtl(true)
                .setOnheapCacheEnabled(true);

        cfg.setCacheConfiguration(new CacheConfiguration[]{
            ccfgMetadta, ccfgAuditLog, ccfgCompany,
            ccfgTenantSSOConf, ccfgTenant, ccfgSSOConf,
            ccfgSecConf
        });

        DataStorageConfiguration dataStoreConf = cfg.getDataStorageConfiguration();

        if (dataStoreConf == null) {
            dataStoreConf = new DataStorageConfiguration();
            cfg.setDataStorageConfiguration(dataStoreConf);
        }

        dataStoreConf.getDefaultDataRegionConfiguration()
                .setPersistenceEnabled(true);

        try {
            cfg.setGridLogger(igniteLog4J2Logger());
        } catch (ConfigurationException ex) {
            if (log.isErrorEnabled()) {
                log.error("Failed to set Ignite grid logger!", ex);
            }
        }

        // TODO: Testing only! Limit discovery to single process within same JVM.
        TcpDiscoverySpi disco = new TcpDiscoverySpi()
                .setIpFinder(new TcpDiscoveryVmIpFinder(true));
        cfg.setDiscoverySpi(disco);

        return cfg;
    }

    /**
     *
     *
     * @return
     */
    @Bean(SEC_CACHE_MGR_BEAN)
    public SpringCacheManager igniteCacheManager() {
        SpringCacheManager cm = new SpringCacheManager();
        cm.setIgniteInstanceName(SEC_SVC_DATA_NODE);
        return cm;
    }

    @Override
    public CacheManager cacheManager() {
        return igniteCacheManager();
    }

    @Override
    public CacheResolver cacheResolver() {
        return new SimpleCacheResolver(cacheManager());
    }

    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler();
    }

}
