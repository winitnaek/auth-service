/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.config;
import com.bsi.sec.config.SecurityServiceProperties;
import static com.bsi.sec.util.AppConstants.BEAN_TPF_DATA_SOURCE;
import static com.bsi.sec.util.AppConstants.BEAN_TPF_ENTITY_MANAGER_FACTORY;
import static com.bsi.sec.util.AppConstants.BEAN_TPF_TRANSACTION_MANAGER_FACTORY;
import static com.bsi.sec.util.AppConstants.SPRING_PROFILE_DEV;
import com.bsi.sec.util.CryptUtils;
import java.util.Arrays;
import java.sql.SQLException;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
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
 * @author Vinit
 */
@Configuration
@PropertySource("classpath:/sws.properties")
@ComponentScan(basePackages = {
    "com.bsi.sec.tpfrepository",
    "com.bsi.sec.exception", "com.bsi.sec.config"
}
)
@EnableJpaRepositories(
        basePackages = {"com.bsi.sec.tpfrepository"},
        entityManagerFactoryRef = BEAN_TPF_ENTITY_MANAGER_FACTORY,
        transactionManagerRef = BEAN_TPF_TRANSACTION_MANAGER_FACTORY
)
public class TPFStoreConfiguration implements WebMvcConfigurer {

    private final static Logger log = LoggerFactory.getLogger(TPFStoreConfiguration.class);
    private static final String JPA_PU_NAME = "sws";
    private static final String[] JPA_ENTITIES_PACKAGES = new String[]{"com.bsi.sec.tpfdomain"};

    @Autowired
    private SecurityServiceProperties props;
    
    @Autowired
    private Environment env;

    @Bean(name = BEAN_TPF_ENTITY_MANAGER_FACTORY)
    @Primary
    public EntityManagerFactory entityManagerFactory() throws SQLException {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setPersistenceUnitName(JPA_PU_NAME);
        factory.setPackagesToScan(JPA_ENTITIES_PACKAGES);
        JpaDialect jpaDialect = new HibernateJpaDialect();
        factory.setJpaDialect(jpaDialect);
        //
        Properties jpaProps = new Properties();
        jpaProps.put("hibernate.dialect", props.getTpfjpa().getDialect());
        jpaProps.put("hibernate.default_schema", props.getTpfjpa().getDefaultSchema());
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

    @Bean(name = BEAN_TPF_TRANSACTION_MANAGER_FACTORY)
    @Primary
    public PlatformTransactionManager transactionManager() throws SQLException {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }

    @Bean(name = BEAN_TPF_DATA_SOURCE, destroyMethod = "close")
    @Primary
    public DataSource dataSource() throws SQLException {
        return createDataSource();
    }
    
    /**
     * createDataSource
     * @return
     * @throws SQLException 
     */
    private DataSource createDataSource() throws SQLException {
                BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName(props.getTpfDataSource().getDriverClassName());
        bds.setUrl(props.getTpfDataSource().getUrl());

        boolean debugMode = props.isDebugMode();

        if (debugMode) {
            String userName = props.getTpfDataSource().getUsername();
            bds.setUsername(userName);
            String password = props.getTpfDataSource().getPassword();
            bds.setPassword(password);
        } else {
            String userName = CryptUtils.aesDecrypt(props.getTpfDataSource()
                    .getUsername(), CryptUtils.TRANSFORMATION_AES_CBC_PKCS5);
            bds.setUsername(userName);
            String password = CryptUtils.aesDecrypt(props.getTpfDataSource()
                    .getPassword(), CryptUtils.TRANSFORMATION_AES_CBC_PKCS5);
            bds.setPassword(password);
        }

        bds.setMaxWaitMillis(props.getTpfDataSource().getMaxWait());
        bds.setMaxTotal(props.getTpfDataSource().getMaxActive());
        bds.setMaxIdle(props.getTpfDataSource().getMaxIdle());
        bds.setRemoveAbandonedTimeout(props.getTpfDataSource().getRemoveAbandonedTimeout());
        bds.setRemoveAbandonedOnMaintenance(props.getTpfDataSource().isRemoveAbandoned());
        bds.setValidationQuery(props.getTpfDataSource().getValidationQuery());
        bds.setValidationQueryTimeout(props.getTpfDataSource().getValidationQueryTimeout());
        bds.setTestOnBorrow(true);
        bds.setTestOnReturn(true);
        bds.setTestWhileIdle(true);
        logDataSourceProperties(bds);
        return bds;
    }
    /**
     * logDataSourceProperties
     * @param bds 
     */
    private void logDataSourceProperties(BasicDataSource bds) {
        if (log.isInfoEnabled()) {
            log.info("############################ TPF Data Source Props ################################");
            log.info("############################ Start ###############################################");
        }
        Arrays.asList(ToStringBuilder.reflectionToString(bds).split(","))
                .forEach(s -> {
                    if (!s.contains("password")) {

                        if (log.isInfoEnabled()) {
                            log.info(s);
                        }
                    } else if (env.acceptsProfiles(Profiles.of(SPRING_PROFILE_DEV))) {
                        if (log.isInfoEnabled()) {
                            log.info(s); // Do not output password unless in DEV!
                        }
                    }
                });

        if (log.isInfoEnabled()) {
            log.info("############################ End ###############################################");
        }
    }
}