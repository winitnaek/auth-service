/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.config.SecurityServiceProperties;
import com.bsi.sec.config.SecurityServiceProperties.SF;
import com.bsi.sec.domain.Tenant;
import com.bsi.sec.exception.ConfigurationException;
import com.bsi.sec.exception.RecordNotFoundException;
import com.bsi.sec.util.DateUtils;
import com.bsi.sec.util.SOQLQueries;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Entitlement;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * Component is responsible for providing functionality to pull data from the
 * Salesforce.
 *
 * @author igorV
 */
@Component
public class SFDataPuller implements DataPuller {

    private final static Logger log = LoggerFactory.getLogger(SFDataPuller.class);

    @Autowired
    private SecurityServiceProperties props;

    @Autowired
    private EntityIDGenerator idGenerator;

    private EnterpriseConnection connection;

    @Override
    public List<Tenant> pullAll(LocalDateTime fromDtTm) throws Exception {
        return getAllActiveEntitlements(fromDtTm);
    }

    @Override
    public List<Object> pullUpdates(LocalDateTime fromDtTm) throws Exception {
        return null;
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void initialize() throws Exception {
        login();
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void postCleanup() throws Exception {
        logout();
    }

    /**
     * Fetches all "SaaS Active" account records from Salesforce.
     *
     * @param fromDtTm
     * @return
     */
    private List<Tenant> getAllActiveEntitlements(LocalDateTime fromDtTm)
            throws ConfigurationException, RecordNotFoundException {
        if (connection == null) {
            throw new ConfigurationException("Salesforce connection must be established!");
        }

        try {
            String queryToUse = getActiveEntitlmentsQuery(fromDtTm);
            QueryResult qr = connection.queryAll(queryToUse);
            boolean done = false;

            if (qr.getSize() == 0) {
                String errMsg = "No entitlements found from Query: "
                        + SOQLQueries.GET_ACTIVE_ENTITLEMENTS;

                if (log.isErrorEnabled()) {
                    log.error(errMsg);
                }

                throw new RecordNotFoundException(errMsg);
            }

            if (log.isDebugEnabled()) {
                log.debug("\nInitial pull...");
                log.debug("\nLogged-in user can see " + qr.getRecords().length + " Entitlement records.");
            }

            List<Tenant> tenants = new ArrayList<>(qr.getSize());

            while (!done) {
                SObject[] records = qr.getRecords();

                for (SObject rec : records) {
                    Entitlement ent = (Entitlement) rec;
                    Tenant tn = new Tenant();
                    tn.setAcctId(ent.getAccount_18_Digit_ID__c());
                    tn.setAcctName(ent.getAccount_Name__c());
                    tn.setDataset(ent.getDataset_1__c());
                    tn.setEnabled(true);
                    tn.setImported(true);
                    tn.setProdName(ent.getProduct_Name__c());
                    tn.setId(idGenerator.generate());
                    tenants.add(tn);

                    if (log.isTraceEnabled()) {
                        log.trace("Tenant: " + tn.toString());
                    }

                }

                if (qr.isDone()) {
                    done = true;
                } else {
                    qr = connection.queryMore(qr.getQueryLocator());
                }
            }

            return tenants;
        } catch (ConnectionException ex) {
            throw new ConfigurationException("Failed while getting active entitlements!", ex);
        }
    }

    /**
     * <p>
     * Establish connection to Salesforce instance.
     * </p>
     */
    private void login() throws ConfigurationException {
        ConnectorConfig config = new ConnectorConfig();
        SF sf = props.getSf();
        config.setUsername(sf.getUsername());
        config.setPassword(sf.getPassword() + sf.getSecToken());

        config.setAuthEndpoint(sf.getAuthEndpoint());

        try {
            connection = new EnterpriseConnection(config);

            if (log.isInfoEnabled()) {
                log.info("Salesforce successful login for {}", sf.toString());
            }
        } catch (ConnectionException ex) {
            throw new ConfigurationException("Failed to connect to Salesforce auth endpoint! "
                    + sf.toString(), ex);
        }
    }

    /**
     * <p>
     * Logs user out of Salesforce instance.
     * </p>
     */
    private void logout() throws ConfigurationException {
        try {
            if (connection != null) {
                connection.logout();

                if (log.isInfoEnabled()) {
                    log.info("Salesforce successful logout for {}",
                            props.getSf().toString());
                }
            }
        } catch (ConnectionException ce) {
            throw new ConfigurationException("Salesforce logout failed! "
                    + "props.getSf().toString()", ce);
        }
    }

    /**
     *
     * @param fromDtTm
     * @return
     */
    private String getActiveEntitlmentsQuery(LocalDateTime fromDtTm) {
        LocalDateTime fromDateTimeToUse = fromDtTm != null ? fromDtTm
                : DateUtils.defaultFromSyncTime();
        String fromDateAsUTC = DateTimeFormatter.ISO_INSTANT
                .format(fromDateTimeToUse.toInstant(ZoneOffset.UTC));
        String queryToUse = SOQLQueries.GET_ACTIVE_ENTITLEMENTS
                .replace(":createddate", fromDateAsUTC);
        return queryToUse;
    }
}
