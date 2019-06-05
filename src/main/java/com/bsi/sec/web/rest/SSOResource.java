package com.bsi.sec.web.rest;

import com.bsi.sec.dto.SSOResult;
import com.bsi.sec.saml.client.SAMLResponseHandler;
import static com.bsi.sec.util.WSConstants.SSO_SERVICE;
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
    /**
     * Processes SAML and provides a result.
     *
     * @param datasetName
     * @return
     */
    @GetMapping("/processSAML ")
    public ResponseEntity<SSOResult> processSAML(
            @Valid @NotNull @RequestParam(required = true) String saml, @RequestParam String relayState) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("REST request to process saml sso");
        }
        SSOResult result = responseHandler.processResponse(saml, relayState);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
