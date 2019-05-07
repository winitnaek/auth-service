/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.dto.DatasetProductDTO;
import com.bsi.sec.dto.SSOConfigDTO;
import com.bsi.sec.dto.TenantDTO;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service responsible for serving REST resource/controller requests!
 *
 * //TODO: Add validation, exception handling, comments.
 *
 * @author igorV
 */
@Service
//@Transactional
public class SecurityService {

    private final static Logger log = LoggerFactory.getLogger(SecurityService.class);

    /**
     *
     * @param dataset
     * @return
     * @throws Exception
     */
    public Set<DatasetProductDTO> getProductsByDataset(String dataset) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("SERVICE invoked to get all Products for the specfied Dataset.");
        }

        // TODO: This is a service stub!!! Provide actual implementation!!!
        if (log.isDebugEnabled()) {
            log.debug("This is a service stub!!! Provide actual implementation!!!");
        }

        Set<DatasetProductDTO> productsToRet = getTestProducts();
        return productsToRet;
    }

    /**
     * TODO: Test-only stub! Add implementation!
     *
     * @return
     */
    public boolean runFullSFSync() throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("SERVICE invoked to run Full Salesforce sync.");
        }

        boolean isFullSFSyncSuccess = true;
        return isFullSFSyncSuccess;
    }

    /**
     *
     * @param fromDateTime
     * @return
     */
    public boolean runPeriodicDataSync(LocalDateTime fromDateTime) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("SERVICE invoked to run Periodic Salesforce sync with args: fromDateTime -> {}",
                    fromDateTime.toInstant(ZoneOffset.UTC).toString());
        }

        boolean isPerSFSyncSuccess = true;
        return isPerSFSyncSuccess;
    }

    /**
     *
     * @param enabled
     * @return
     */
    public boolean enablePeriodicDataSync(boolean enabled) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("SERVICE invoked to enable/disable Periodic Data Sync with args: enabled -> {}",
                    Boolean.valueOf(enabled).toString());
        }

        boolean isSFSyncEnabled = true;
        return isSFSyncEnabled;
    }

    /**
     *
     * @param accountName
     * @param productName
     * @param datasetName
     * @return
     */
    public TenantDTO createTenant(String accountName, String productName,
            String datasetName) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("SERVICE invoked to add new Tenant with Account Name = {}, "
                    + "Product Name = {}, Dataset Name = {}", accountName,
                    productName, datasetName);
        }

        TenantDTO tenant = new TenantDTO(1L, "BSI Inc.",
                "TPF", "BSI_DSET_1");
        return tenant;
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean deleteTenant(Long id) {
        if (log.isDebugEnabled()) {
            log.debug("SERVICE invoked to delete Tenant with ID = {}", id);
        }

        boolean isDeleted = true;
        return isDeleted;
    }

    /**
     *
     * @param ssoConfig
     * @return
     */
    public SSOConfigDTO createSSOConfig(SSOConfigDTO ssoConfig) {
        SSOConfigDTO config = new SSOConfigDTO();
        config.setAcctName("BSI");
        config.setDsplName("BSI SSO Config");
        config.setEnabled(true);
        return config;
    }

    /**
     *
     * @param ssoConfig
     * @return
     */
    public SSOConfigDTO updateSSOConfig(SSOConfigDTO ssoConfig) {
        SSOConfigDTO config = new SSOConfigDTO();
        config.setAcctName("BSI");
        config.setDsplName("BSI SSO Config 2");
        config.setEnabled(true);
        return config;
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean deleteSSOConfig(long id) {
        //TODO: Add implementation!
        return true;
    }

    /**
     *
     * @param accountName
     * @param ssoConfigId
     * @param toUnlink
     * @return
     */
    public boolean linkSSOConfigToTenant(String accountName, long ssoConfigId, boolean toUnlink) {
        //TODO: Add implementation!
        return true;
    }

    public boolean testSSOConfiguration(String accountName, long ssoConfigId) {
        //TODO: Add implementation!
        return true;
    }

    /**
     * TODO: Test-only stub! Add implementation!
     *
     * @param productsToRet
     */
    private Set<DatasetProductDTO> getTestProducts() throws Exception {
        String[][] dsetsProds = new String[][]{
            {"DSET1", "TPF", "ACCT1"},
            {"DSET2", "TF", "ACCT2"},
            {"DSET3", "CF", "ACCT3"}};
        return Arrays.asList(dsetsProds).stream().map(dp
                -> new DatasetProductDTO(1L, dp[2], dp[1], dp[0])).collect(Collectors.toSet());
    }

}
