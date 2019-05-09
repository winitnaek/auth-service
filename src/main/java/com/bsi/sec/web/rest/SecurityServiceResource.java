package com.bsi.sec.web.rest;

import com.bsi.sec.dto.AuditLogDTO;
import com.bsi.sec.dto.DatasetProductDTO;
import com.bsi.sec.dto.ProductDTO;
import com.bsi.sec.dto.SSOConfigDTO;
import com.bsi.sec.dto.SyncInfoDTO;
import com.bsi.sec.dto.TenantDTO;
import com.bsi.sec.svc.SecurityService;
import static com.bsi.sec.util.WSConstants.SECURITY_SERVICE;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

/**
 * REST controller for managing Tenant.
 *
 * //TODO: Add JSR-303 validation, exception handling, comments.
 *
 */
@RestController
@Validated
@RequestMapping(SECURITY_SERVICE)
public class SecurityServiceResource {

    private final static Logger log = LoggerFactory.getLogger(SecurityServiceResource.class);

    @Autowired
    private SecurityService securityService;

    /**
     * Retrieves products associated with the given dataset.
     *
     * @param datasetName
     * @return
     */
    @GetMapping("/getProductsByDataset")
    public ResponseEntity<Set<DatasetProductDTO>> getProductsByDataset(
            @Valid @NotNull @RequestParam(required = true) String datasetName) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get all Products for the specfied Dataset.");
        }

        Set<DatasetProductDTO> productsToRet = securityService.getProductsByDataset(datasetName);
        return new ResponseEntity<>(productsToRet, HttpStatus.OK);
    }

    /**
     *
     * @return @throws Exception
     */
    @PostMapping("/runInitialDataSync")
    public ResponseEntity<Boolean> runInitialDataSync() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to run Full Salesforce sync.");
        }

        boolean status = securityService.runFullSFSync();
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     *
     * @param fromDateTime
     * @return
     * @throws Exception
     */
    @PostMapping("/runPeriodicDataSync")
    public ResponseEntity<Boolean> runPeriodicDataSync(@Valid @NotNull @RequestParam(required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDateTime) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to run Periodic Salesforce sync.");
        }

        boolean status = securityService.runPeriodicDataSync(fromDateTime);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     *
     * @param enabled
     * @return
     * @throws Exception
     */
    @PostMapping("/enablePeriodicDataSync")
    public ResponseEntity<Boolean> enablePeriodicDataSync(
            @RequestParam boolean enabled) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to enable/disable Periodic Data Sync.");
        }

        boolean status = securityService.enablePeriodicDataSync(enabled);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     *
     * @param accountName
     * @param productName
     * @param datasetName
     * @return
     * @throws Exception
     */
    @PostMapping("/addTenant")
    public ResponseEntity<TenantDTO> addTenant(
            @Valid @NotNull @RequestParam(required = true) String accountName,
            @Valid @NotNull @RequestParam(required = true) String productName,
            @Valid @NotNull @RequestParam(required = true) String datasetName) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to add new Tenant with Account Name = {}, "
                    + "Product Name = {}, Dataset Name = {}", accountName,
                    productName, datasetName);
        }

        TenantDTO tenant = securityService.createTenant(accountName,
                productName, datasetName);
        return new ResponseEntity<>(tenant, HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/deleteTenant")
    public ResponseEntity<Boolean> deleteTenant(
            @Valid @Min(1L) @RequestParam(required = true) Long id) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to delete Tenant with ID = {}", id);
        }

        boolean isDeleted = securityService.deleteTenant(id);
        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

    /**
     *
     * @param ssoConfig
     * @return
     * @throws Exception
     */
    @PostMapping("/addSSOConfig")
    public ResponseEntity<SSOConfigDTO> addSSOConfig(
            @Valid @NotNull @RequestBody(required = true) SSOConfigDTO ssoConfig) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to add new SSO Configuration = {}",
                    ssoConfig.toString());
        }

        SSOConfigDTO config = securityService.createSSOConfig(ssoConfig);
        return new ResponseEntity<>(config, HttpStatus.OK);
    }

    /**
     *
     * @param ssoConfig
     * @return
     * @throws Exception
     */
    @PostMapping("/updateSSOConfig")
    public ResponseEntity<SSOConfigDTO> updateSSOConfig(
            @Valid @NotNull @RequestBody(required = true) SSOConfigDTO ssoConfig) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to update existing SSO Configuration = {}",
                    ssoConfig.toString());
        }

        SSOConfigDTO config = securityService.updateSSOConfig(ssoConfig);
        return new ResponseEntity<>(config, HttpStatus.OK);
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/deleteSSOConfig")
    public ResponseEntity<Boolean> deleteSSOConfig(
            @Valid @Min(1L) @RequestParam(required = true) long id) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to delete existing SSO Configuration "
                    + "with ID = {}", id);
        }

        boolean isDeleted = securityService.deleteSSOConfig(id);
        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

    /**
     *
     * @param accountName
     * @param ssoConfigId
     * @param toUnlink
     * @return
     * @throws Exception
     */
    @PostMapping("/linkSSOConfigToTenant")
    public ResponseEntity<Boolean> linkSSOConfigToTenant(
            @Valid @NotNull @RequestParam(required = true) String accountName,
            @Valid @Min(1L) @RequestParam(required = true) long ssoConfigId,
            @RequestParam boolean toUnlink) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to link existing SSO Configuration to Tenant"
                    + "with Account Name = {}, SSO Config Id = {}, Unlink flag = {}",
                    accountName, ssoConfigId, toUnlink);
        }

        boolean isDeleted = securityService.linkSSOConfigToTenant(accountName,
                ssoConfigId, toUnlink);
        return new ResponseEntity<>(isDeleted, HttpStatus.OK);
    }

    /**
     *
     * @param accountName
     * @param ssoConfigId
     * @return
     * @throws Exception
     */
    @GetMapping("/testSSOConfiguration")
    public ResponseEntity<Boolean> testSSOConfiguration(
            @Valid @NotNull @RequestParam(required = true) String accountName,
            @Valid @Min(1L) @RequestParam(required = true) long ssoConfigId)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to test existing SSO Configuration"
                    + "with Account Name = {}, SSO Config Id = {}",
                    accountName, ssoConfigId);
        }

        boolean isValid = securityService.testSSOConfiguration(accountName,
                ssoConfigId);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }

    /**
     *
     * @param lastNoDays
     * @return
     * @throws Exception
     */
    @GetMapping("/getAuditLogs")
    public ResponseEntity<AuditLogDTO> getAuditLogs(
            @Valid @Min(1L) @RequestParam(required = true) int lastNoDays)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get Audit Logs "
                    + "for last {} days",
                    lastNoDays);
        }

        AuditLogDTO auditLogs = securityService.getAuditLogs(lastNoDays);
        return new ResponseEntity<>(auditLogs, HttpStatus.OK);
    }

    /**
     *
     * @param accountName
     * @return
     * @throws Exception
     */
    @GetMapping("/getSSOConfigsByTenant")
    public ResponseEntity<SSOConfigDTO> getSSOConfigsByTenant(
            @Valid @NotNull @RequestParam(required = true) String accountName)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get SSO Configuration by Tenant "
                    + "with Account Name = {}", accountName);
        }

        SSOConfigDTO ssoConfigDTO = securityService
                .getSSOConfigsByTenant(accountName);
        return new ResponseEntity<>(ssoConfigDTO, HttpStatus.OK);
    }

    /**
     *
     * @param includeImported
     * @return
     * @throws Exception
     */
    @GetMapping("/getTenants")
    public ResponseEntity<TenantDTO> getTenants(boolean includeImported)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get Tenants "
                    + "with Include Imported flag = {}", includeImported);
        }

        TenantDTO tenantDTO = securityService
                .getTenants(includeImported);
        return new ResponseEntity<>(tenantDTO, HttpStatus.OK);
    }

    /**
     *
     * @return @throws Exception
     */
    @GetMapping("/getLastSyncInfo")
    public ResponseEntity<SyncInfoDTO> getLastSyncInfo()
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get Last Data Sync Info.");
        }

        SyncInfoDTO syncInfoDTO = securityService.getLastSyncInfo();
        return new ResponseEntity<>(syncInfoDTO, HttpStatus.OK);
    }

    /**
     *
     * @return @throws Exception
     */
    @GetMapping("/getProductsByTenants")
    public ResponseEntity<ProductDTO> getProductsByTenants()
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get Last Data Sync Info.");
        }

        ProductDTO prodInfoDTO = securityService.getProductsByTenants();
        return new ResponseEntity<>(prodInfoDTO, HttpStatus.OK);
    }

}
