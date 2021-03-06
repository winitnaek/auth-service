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
import com.bsi.sec.exception.ProcessingException;
import com.bsi.sec.exception.RecordNotFoundException;
import com.bsi.sec.util.CryptUtils;
import com.bsi.sec.util.DateUtils;
import com.bsi.sec.util.LogUtils;
import com.bsi.sec.util.SOQLQueries;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.GetUpdatedResult;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Entitlement;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
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
        refreshSessionCheck();
        return getAllActiveEntitlements(fromDtTm);
    }

    @Override
    public List<Tenant> pullUpdates(LocalDateTime fromDtTm) throws Exception {
        refreshSessionCheck();
        return getUpdatedEntitlements(fromDtTm);
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
     * Checks if current user's session is about to expire and reestablishes the
     * session.
     *
     * @throws Exception
     */
    public void refreshSessionCheck() throws Exception {
        try {
            if (connection != null) {
                int secondsSessValid = 0;

                try {
                    secondsSessValid = connection.getUserInfo().getSessionSecondsValid();
                } catch (ConnectionException ex) {
                    // Attempt logging in to SF!
                    login();
                    secondsSessValid = connection.getUserInfo().getSessionSecondsValid();
                }

                int secsBeforeSessExpire = 10 * 60; // 10 minutes

                if (secondsSessValid <= secsBeforeSessExpire) {
                    if (log.isInfoEnabled()) {
                        log.info(LogUtils.jsonize("msg", "Session must be refreshed!",
                                "secsBeforeSessExpireTresholdInSecs", secsBeforeSessExpire, "sessStillValidForInSecs",
                                secondsSessValid, "url", connection.getConfig().getAuthEndpoint(), "sessionid",
                                connection.getConfig().getSessionId(), "userid", connection.getUserInfo().getUserId()));
                    }

                    logout();
                    login();

                    if (log.isInfoEnabled()) {
                        log.info(LogUtils.jsonize("msg", "Session has been refreshed!", "url",
                                connection.getConfig().getAuthEndpoint(), "sessionid",
                                connection.getConfig().getSessionId(), "userid", connection.getUserInfo().getUserId()));
                    }
                }
            }
        } catch (ConnectionException ex) {
            throw new ConfigurationException("Failed to refresh SF session!", ex);
        }
    }

    /**
     * Fetches all "SaaS Active" account records from Salesforce.
     *
     * @param fromDtTm
     * @return
     */
    private List<Tenant> getAllActiveEntitlements(LocalDateTime fromDtTm) throws Exception {
        if (connection == null) {
            String errMsg = "Salesforce connection must be established!";

            if (log.isErrorEnabled()) {
                log.error(errMsg);
            }

            throw new ConfigurationException(errMsg);
        }

        try {
            String queryToUse = getActiveEntitlmentsQuery(fromDtTm);
            QueryResult qr = connection.queryAll(queryToUse);
            boolean done = false;

            if (qr.getSize() == 0) {
                String errMsg = "No entitlements found from Query: " + SOQLQueries.GET_ACTIVE_ENTITLEMENTS;

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
            Set<String> recKeys = new HashSet<String>(tenants.size());

            while (!done) {
                SObject[] records = qr.getRecords();

                for (SObject rec : records) {
                    Entitlement ent = (Entitlement) rec;

                    if (dupTenantKeyFound(recKeys, ent)) {
                        continue;
                    }

                    Tenant tn = new Tenant();
                    tn.setAcctId(ent.getAccount_18_Digit_ID__c());
                    tn.setAcctName(ent.getAccount_Name__c());
                    tn.setDataset(ent.getDataset_1__c());
                    tn.setEnabled(true);
                    tn.setImported(true);
                    tn.setProdId(ent.getProduct__c());
                    tn.setProdName(ent.getProduct_Name__c());
                    tn.setId(idGenerator.generate());
                    tenants.add(tn);
                    addTenantKey(recKeys, ent);

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
            throw new ConfigurationException("SF connection exception!", ex);
        } catch (Exception ex) {
            throw new ProcessingException("Failed while getting active entitlements!", ex);
        }
    }

    /**
     * Fetches updated "SaaS Active" account records from Salesforce.
     *
     * @param fromDtTm
     * @return
     */
    private List<Tenant> getUpdatedEntitlements(LocalDateTime fromDtTm) throws Exception {
        if (connection == null) {
            String errMsg = "Salesforce connection must be established!";

            if (log.isErrorEnabled()) {
                log.error(errMsg);
            }

            throw new ConfigurationException(errMsg);
        }

        try {
            Date fromDate = Date.from(fromDtTm.toInstant(ZoneOffset.UTC));
            GregorianCalendar from = (GregorianCalendar) Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            from.setTime(fromDate);
            GregorianCalendar now = (GregorianCalendar) Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            //
            GetUpdatedResult ur = connection.getUpdated("Entitlement", from, now);
            String[] updIDs = ur.getIds();

            if (log.isDebugEnabled()) {
                log.debug("There are {} updated Entitlement records returned"
                        + " by SF connection.getUpdated(...) API. Not that"
                        + " these records (all or some or none) may not be"
                        + " related to qualified SaaS enabled Tenants!", updIDs.length);
            }

            List<Tenant> tenants = new ArrayList<>(updIDs.length);

            for (String id : updIDs) {
                String queryToUse = getActiveEntitlmentByIdQuery(id, fromDtTm);
                QueryResult qr = connection.query(queryToUse);

                if (qr.getSize() == 0) {
                    if (log.isDebugEnabled()) {
                        log.debug(LogUtils.jsonize("msg", "Unrelated updated SF:Entitlement" + " record found!", "ID",
                                id));
                    }

                    continue;
                }

                SObject[] records = qr.getRecords();

                if (records.length > 0) {
                    Entitlement ent = (Entitlement) records[0];
                    Tenant tn = new Tenant();
                    tn.setAcctId(ent.getAccount_18_Digit_ID__c());
                    tn.setAcctName(ent.getAccount_Name__c());
                    tn.setDataset(ent.getDataset_1__c());
                    tn.setEnabled(true);
                    tn.setImported(true);
                    tn.setProdName(ent.getProduct_Name__c());
                    tn.setProdId(ent.getProduct__c());
                    tn.setId(idGenerator.generate());
                    tenants.add(tn);

                    if (log.isTraceEnabled()) {
                        log.trace("Tenant: " + tn.toString());
                    }
                }
            }

            if (log.isInfoEnabled()) {
                log.info("There are {} updated Qualifying Tenant records found!", tenants.size());
            }

            return tenants;
        } catch (ConnectionException ex) {
            throw new ConfigurationException("SF connection exception!", ex);
        } catch (Exception ex) {
            throw new ProcessingException("Failed while getting updated entitlements!", ex);
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

        String username;
        String password;
        String secToken;

        if (props.isDebugMode()) {
            username = sf.getUsername();
            password = sf.getPassword();
            secToken = sf.getSecToken();
        } else {
            username = CryptUtils.aesDecrypt(sf.getUsername(), CryptUtils.TRANSFORMATION_AES_CBC_PKCS5);
            password = CryptUtils.aesDecrypt(sf.getPassword(), CryptUtils.TRANSFORMATION_AES_CBC_PKCS5);
            secToken = CryptUtils.aesDecrypt(sf.getSecToken(), CryptUtils.TRANSFORMATION_AES_CBC_PKCS5);
        }

        config.setUsername(username);
        config.setPassword(password + secToken);

        config.setAuthEndpoint(sf.getAuthEndpoint());

        try {
            connection = new EnterpriseConnection(config);

            if (log.isInfoEnabled()) {
                log.info("Salesforce successful login for {}", sf.toString());
            }
        } catch (ConnectionException ex) {
            throw new ConfigurationException("Failed to connect to Salesforce auth endpoint! " + sf.toString(), ex);
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
                    log.info("Salesforce successful logout for {}", props.getSf().toString());
                }
            }
        } catch (ConnectionException ce) {
            throw new ConfigurationException("Salesforce logout failed! " + "props.getSf().toString()", ce);
        }
    }

    /**
     *
     * @param fromDtTm
     * @return
     */
    private String getActiveEntitlmentsQuery(LocalDateTime fromDtTm) {
        LocalDateTime fromDateTimeToUse = fromDtTm != null ? fromDtTm : DateUtils.defaultFromSyncTime();
        String fromDateAsUTC = DateTimeFormatter.ISO_INSTANT.format(fromDateTimeToUse.toInstant(ZoneOffset.UTC));
        String queryToUse = SOQLQueries.GET_ACTIVE_ENTITLEMENTS.replace(":createddate", fromDateAsUTC);
        return queryToUse;
    }

    /**
     *
     * @param id
     * @param fromDtTm
     * @return
     */
    private String getActiveEntitlmentByIdQuery(String id, LocalDateTime fromDtTm) {
        LocalDateTime fromDateTimeToUse = fromDtTm != null ? fromDtTm : DateUtils.defaultFromSyncTime();
        String fromDateAsUTC = DateTimeFormatter.ISO_INSTANT.format(fromDateTimeToUse.toInstant(ZoneOffset.UTC));
        String queryToUse = SOQLQueries.GET_ENTITLEMENTS_BY_ID.replace(":id", id).replace(":createddate",
                fromDateAsUTC);
        return queryToUse;
    }

    /**
     * Adds given Entitlement <source>ent</scource> key to the sets of keys.
     *
     * @param recKeys
     * @param ent
     */
    private void addTenantKey(Set<String> recKeys, Entitlement ent) {
        recKeys.add(buildTenantKey(ent));
    }

    /**
     * Builds key string.
     *
     * @param ent
     * @return
     */
    private String buildTenantKey(Entitlement ent) {
        return StringUtils.trimToEmpty(ent.getDataset_1__c()) + StringUtils.trimToEmpty(ent.getProduct_Name__c())
                + StringUtils.trimToEmpty(ent.getAccount_Name__c());
    }

    /**
     * Checks if incoming tenant's key already found in <source>recKeys</source>.
     *
     * @param recKeys
     * @param ent
     * @return
     */
    private boolean dupTenantKeyFound(Set<String> recKeys, Entitlement ent) {
        return recKeys.contains(buildTenantKey(ent));
    }

}
