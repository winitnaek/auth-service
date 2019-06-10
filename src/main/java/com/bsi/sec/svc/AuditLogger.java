/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.config.UserProvider;
import com.bsi.sec.domain.AdminMetadata;
import com.bsi.sec.domain.AuditLog;
import com.bsi.sec.domain.Company;
import com.bsi.sec.domain.SSOConfiguration;
import com.bsi.sec.domain.Tenant;
import com.bsi.sec.dto.AuditLogDTO;
import com.bsi.sec.repository.AuditLogRepository;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Component responsible for calling operations to log CRUD related activity.
 *
 * @author igorV
 */
@Component
@Transactional
public class AuditLogger implements UserProvider {

    private final static Logger log = LoggerFactory.getLogger(AuditLogger.class);

    private final static String ALL_RECS = "ALL";

    public enum Areas {
        TENANT, COMPANY, SSO_CONF, TENANT_SSO_CONF, ADMIN_META_DATA, MGMT_UI
    }

    public enum Ops {
        INSERT, UPDATE, UPSERT, DELETE, LOGIN, LOGOUT
    }

    @Autowired
    private AuditLogRepository auditLogRepo;

    @Autowired
    private EntityIDGenerator idGen;

    @Autowired
    private IgniteConfiguration ignConf;

    /**
     *
     * @return
     */
    @Transactional
    public boolean logUserLoggedIn(String username) {
        AuditLogDTO logInput = new AuditLogDTO();
        logInput.setAccount("");
        logInput.setArea(Areas.MGMT_UI);
        logInput.setDataset("");
        logInput.setOperation(Ops.LOGIN);
        logInput.setProduct("");
        logInput.setServerHost(ignConf.getLocalHost());
        logInput.setId(idGen.generate());
        logInput.setUser(StringUtils.isNotBlank(username) ? username : SERVICE_USER);

        if (log.isDebugEnabled()) {
            log.debug("Candidate to be logged {}.", logInput.toString());
        }

        return log(logInput);
    }

    /**
     *
     * @return
     */
    @Transactional
    public boolean logUserLoggedOut(String username) {
        AuditLogDTO logInput = new AuditLogDTO();
        logInput.setAccount("");
        logInput.setArea(Areas.MGMT_UI);
        logInput.setDataset("");
        logInput.setOperation(Ops.LOGOUT);
        logInput.setProduct("");
        logInput.setServerHost(ignConf.getLocalHost());
        logInput.setId(idGen.generate());
        logInput.setUser(StringUtils.isNotBlank(username) ? username : SERVICE_USER);

        if (log.isDebugEnabled()) {
            log.debug("Candidate to be logged {}.", logInput.toString());
        }

        return log(logInput);
    }

    /**
     *
     * @param areas
     * @param ops
     * @return
     */
    @Transactional
    public boolean logAll(Areas area, Ops op) {
        AuditLogDTO logInput = new AuditLogDTO();
        logInput.setAccount(ALL_RECS);
        logInput.setArea(area);
        logInput.setDataset(ALL_RECS);
        logInput.setOperation(op);
        logInput.setProduct(ALL_RECS);
        logInput.setServerHost(ignConf.getLocalHost());
        logInput.setId(idGen.generate());
        logInput.setUser(getUser());

        if (log.isDebugEnabled()) {
            log.debug("Candidate to be logged {}.", logInput.toString());
        }

        return log(logInput);
    }

    /**
     *
     * @param tenUpd,
     */
    @Transactional
    public boolean logEntity(Tenant ent, Areas area, Ops op) {
        AuditLogDTO logInput = new AuditLogDTO();
        logInput.setAccount(ent.getAcctName());
        logInput.setArea(area);
        logInput.setDataset(ent.getDataset());
        logInput.setOperation(op);
        logInput.setProduct(ent.getProdName());
        logInput.setServerHost(ignConf.getLocalHost());
        logInput.setId(idGen.generate());
        logInput.setUser(getUser());

        if (log.isDebugEnabled()) {
            log.debug("Candidate to be logged {}.", logInput.toString());
        }

        return log(logInput);
    }

    /**
     *
     * @param tenUpd,
     */
    @Transactional
    public boolean logEntity(Company ent, Areas area, Ops op) {
        AuditLogDTO logInput = new AuditLogDTO();
        logInput.setAccount(" ");
        logInput.setArea(area);
        logInput.setDataset(ent.getDataset());
        logInput.setOperation(op);
        logInput.setProduct(" ");
        logInput.setServerHost(ignConf.getLocalHost());
        logInput.setId(idGen.generate());
        logInput.setUser(getUser());

        if (log.isDebugEnabled()) {
            log.debug("Candidate to be logged {}.", logInput.toString());
        }

        return log(logInput);
    }

    /**
     *
     * @param ent
     * @param area
     * @param op
     * @return
     */
    @Transactional
    public boolean logEntity(AdminMetadata ent, Areas area, Ops op) {
        AuditLogDTO logInput = new AuditLogDTO();
        logInput.setAccount(" ");
        logInput.setArea(area);
        logInput.setDataset(" ");
        logInput.setOperation(op);
        logInput.setProduct(" ");
        logInput.setServerHost(ignConf.getLocalHost());
        logInput.setId(idGen.generate());
        logInput.setUser(getUser());

        if (log.isDebugEnabled()) {
            log.debug("Candidate to be logged {}.", logInput.toString());
        }

        return log(logInput);
    }

    /**
     *
     * @param ent
     * @param area
     * @param op
     * @return
     */
    @Transactional
    public boolean logEntity(SSOConfiguration ent, Areas area, Ops op) {
        AuditLogDTO logInput = new AuditLogDTO();
        logInput.setAccount(ent.getTenant().getAcctName());
        logInput.setArea(area);
        logInput.setDataset(ent.getTenant().getDataset());
        logInput.setOperation(op);
        logInput.setProduct(ent.getTenant().getProdName());
        logInput.setServerHost(ignConf.getLocalHost());
        logInput.setId(idGen.generate());
        logInput.setUser(getUser());

        if (log.isDebugEnabled()) {
            log.debug("Candidate to be logged {}.", logInput.toString());
        }

        return log(logInput);
    }

    /**
     *
     * @param input
     * @return
     */
    private String buildMessage(AuditLog input) {
        try {
            return "User [" + input.getUser() + "]"
                    + " Area [" + input.getArea() + "]:"
                    + " Ran operation["
                    + input.getOperation() + "] at "
                    + input.getCreatedDate();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Failed to build message.", e);
            }

            return "Failed to populate the message.";
        }
    }

    /**
     *
     * @param input
     * @return
     */
    private boolean log(AuditLogDTO input) {
        AuditLog ent = new AuditLog();
        ent.setAccountName(input.getAccount());
        ent.setArea(getArea(input.getArea()));
        ent.setDatasetName(input.getDataset());
        ent.setOperation(getOp(input.getOperation()));
        ent.setProductName(input.getProduct());
        ent.setServerHost(input.getServerHost());
        ent.setUser(input.getUser());
        long id = input.getId();
        ent.setId(id);

        LocalDateTime instant = ent.getCreatedDate();

        if (instant == null) {
            instant = LocalDateTime.now();
            ent.setCreatedDate(instant);
        }

        ent.setMessage(buildMessage(ent));
        AuditLog entUpd = auditLogRepo.save(id, ent);

        if (log.isDebugEnabled()) {
            log.debug("Logged {} to AuditLog.", entUpd.toString());
        }

        return entUpd != null;
    }

    /**
     *
     * @param area
     * @return
     */
    private String getArea(Areas area) {
        if (Areas.COMPANY.equals(area)) {
            return "COMPANY";
        } else if (Areas.SSO_CONF.equals(area)) {
            return "SSO_CONF";
        } else if (Areas.ADMIN_META_DATA.equals(area)) {
            return "ADMIN_META_DATA";
        } else if (Areas.TENANT.equals(area)) {
            return "TENANT";
        } else {
            return "MGMT_UI";
        }
    }

    private String getOp(Ops op) {
        if (Ops.DELETE.equals(op)) {
            return "DELETE";
        } else if (Ops.INSERT.equals(op)) {
            return "INSERT";
        } else if (Ops.UPDATE.equals(op)) {
            return "UPDATE";
        } else if (Ops.UPSERT.equals(op)) {
            return "UPSERT";
        } else if (Ops.LOGIN.equals(op)) {
            return "LOGIN";
        } else {
            return "LOGOUT";
        }
    }

}
