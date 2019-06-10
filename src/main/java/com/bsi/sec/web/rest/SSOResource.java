package com.bsi.sec.web.rest;

import com.bsi.sec.dto.ProductDTO;
import com.bsi.sec.dto.SSOResult;
import com.bsi.sec.saml.client.SAMLResponseHandler;
import com.bsi.sec.svc.SecurityService;
import static com.bsi.sec.util.WSConstants.SSO_SERVICE;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

/**
 * REST controller for processing SSO/SAML.
 *
 *
 *
 */
@RestController
@Validated
@RequestMapping(SSO_SERVICE)
public class SSOResource {

    private final static Logger log = LoggerFactory.getLogger(SSOResource.class);

    @Autowired
    private SAMLResponseHandler responseHandler;

    @Autowired
    private SecurityService securityService;

    /**
     * Processes SAML and provides a result.
     *
     * @param datasetName
     * @return
     */
    @GetMapping("/processSAML")
    public ResponseEntity<SSOResult> processSAML(
            @Valid @NotNull @RequestParam(required = true) String saml, @RequestParam String relayState) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("REST request to process saml sso");
        }
        SSOResult result = responseHandler.processResponse(saml, relayState);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Retrieves products associated with the given dataset.
     *
     * @param datasetName
     * @return
     */
    @GetMapping("/getProductsByDataset")
    public ResponseEntity<List<ProductDTO>> getProductsByDataset(
            @Valid @NotNull @RequestParam(required = true) String datasetName) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("REST request to get all Products for the specfied Dataset.");
        }

        List<ProductDTO> productsToRet = securityService.getProductsByDataset(datasetName);
        return new ResponseEntity<>(productsToRet, HttpStatus.OK);
    }
}
