/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.util;

/**
 *
 * @author igorV
 */
public final class AppConstants {

    // Configuration
    public static final String BEAN_SSO_SERVLET = "ssoServlet";

    // Spring Profiles
    public final static String SPRING_PROFILE_DEV = "dev";
    public final static String SPRING_PROFILE_PROD = "prod";

    // General
    public final static int FAILURE = 0;
    public final static int SUCCESS = 1;

    // TPF Configuration
    public static final String BEAN_TPF_ENTITY_MANAGER_FACTORY = "tpfEntityManagerFactory";
    public static final String BEAN_TPF_TRANSACTION_MANAGER_FACTORY = "tpfJpaTransactionManager";
    public static final String BEAN_TPF_DS_TRANSACTION_MGR_FACTORY = "tpfDsTransactionManager";

    public static final String BEAN_TPF_JOB_RUNNER_EXEC = "tpfJobRunnerExecutor";
    public static final String BEAN_TPF_DATA_SOURCE = "tpfDataSource";

    public static final String BEAN_IGNITE_TX_MGR = "igniteSpringTransactionManager";

    public static final String BEAN_IGNITE_JOB_RUNNER_EXEC = "igniteJobRunnerExecutor";
    public static final String BEAN_IGNITE_DATA_SOURCE = "igniteDataSource";

}
