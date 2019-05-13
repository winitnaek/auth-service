/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.config.SecurityServiceProperties;
import com.bsi.sec.config.SecurityServiceProperties.SF;
import com.bsi.sec.domain.AdminMetadata;
import com.bsi.sec.domain.Tenant;
import com.bsi.sec.dto.DataSyncResponse;
import com.bsi.sec.exception.ConfigurationException;
import com.bsi.sec.exception.RecordNotFoundException;
import com.bsi.sec.repository.AdminMetadataRepository;
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
import java.util.Optional;
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
public class SFDataPuller implements DataSync {

    private final static Logger log = LoggerFactory.getLogger(SFDataPuller.class);

    @Autowired
    private SecurityServiceProperties props;

    @Autowired
    private EntityIDGenerator idGenerator;

    @Autowired
    private AdminMetadataRepository adminMetaDataRepo;

    private EnterpriseConnection connection;

    @Override
    public DataSyncResponse runInitialSync(LocalDateTime fromDateTime) throws Exception {
        DataSyncResponse response;
        Optional<LocalDateTime> lastInitDataSyncDtOpt = getLastInitDataSyncDateTime();

        if (lastInitDataSyncDtOpt.isPresent()) {
            LocalDateTime lastInitDataSyncDt = lastInitDataSyncDtOpt.get();
            response = new DataSyncResponse();
            response.setLastRunDateTime(lastInitDataSyncDt);
            response.setIsSucessfull(true);

            String msg = "Last full SF data sync ran at " + lastInitDataSyncDt;
            response.setMessage(msg);

            if (log.isInfoEnabled()) {
                log.info(msg);
            }

            return response;
        }

        if (log.isInfoEnabled()) {
            log.info("Starting initial data sync from {}", fromDateTime
                    .toInstant(ZoneOffset.UTC).toString());
        }

        List<Tenant> allTenants = getAllActiveEntitlements(fromDateTime);

        response = new DataSyncResponse();
        return response;
    }

    @Override
    public DataSyncResponse runPeriodicSync(LocalDateTime fromDateTime) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void initializeSync() throws Exception {
        login();
    }

    /**
     *
     * @throws Exception
     */
    @Override
    public void postSync() throws Exception {
        logout();
    }

    /**
     *
     * @param fromDateTime
     * @return
     */
    private List<Tenant> getAllActiveEntitlements(LocalDateTime fromDateTime)
            throws ConfigurationException, RecordNotFoundException {
        if (connection == null) {
            throw new ConfigurationException("Salesforce connection must be established!");
        }

        try {
            LocalDateTime fromDateTimeToUse = fromDateTime != null ? fromDateTime
                    : DateUtils.defaultFromSyncTime();
            String fromDateAsUTC = DateTimeFormatter.ISO_INSTANT
                    .format(fromDateTimeToUse.toInstant(ZoneOffset.UTC));
            String queryToUse = SOQLQueries.GET_ACTIVE_ENTITLEMENTS
                    .replace(":createddate", fromDateAsUTC);
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
                log.debug("\\nInitial pull...");
                log.debug("\nLogged-in user can see " + qr.getRecords().length + " Entitlement records.");
            }

            List<Tenant> tenants = new ArrayList<>(qr.getSize());

            while (!done) {
                SObject[] records = qr.getRecords();

                for (SObject rec : records) {
                    Entitlement ent = (Entitlement) rec;
                    Tenant tn = new Tenant();
                    tn.setId(idGenerator.generate());
                    tn.setAcctId(ent.getAccount_18_Digit_ID__c());
                    tn.setAcctName(ent.getAccount_Name__c());
                    tn.setDataset(ent.getDataset_1__c());
                    tn.setEnabled(true);
                    tn.setImported(true);
                    tn.setProdName(ent.getProduct_Name__c());
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
     * <p>
     * Retrieves latest full SF data sync date/time.</p>
     *
     * @return
     */
    private Optional<LocalDateTime> getLastInitDataSyncDateTime() {
        Optional<AdminMetadata> admMetaOpt = adminMetaDataRepo.findById(1L);

        if (!admMetaOpt.isPresent()) {
            return Optional.empty();
        }

        AdminMetadata admMeta = admMetaOpt.get();
        LocalDateTime lastFullSync = admMeta.getLastFullSync();
        return lastFullSync != null ? Optional.of(lastFullSync) : Optional.empty();
    }

}
