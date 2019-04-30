package com.bsi.sec.web.rest;

import com.bsi.sec.dto.DatasetProductDTO;
import com.bsi.sec.svc.SecurityService;
import static com.bsi.sec.util.WSConstants.SECURITY_SERVICE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * REST controller for managing Tenant.
 */
@RestController
@RequestMapping(SECURITY_SERVICE)
public class SecurityServiceResource {

    private final Logger log = LoggerFactory.getLogger(SecurityServiceResource.class);

    @Autowired
    private SecurityService securityService;

    /**
     * Retrieves products associated with the given dataset.
     *
     * @param dataset
     * @return
     */
    @GetMapping("/getProductsByDataset")
    public ResponseEntity<Set<DatasetProductDTO>> getProductsByDataset(
            @RequestParam(required = true) String dataset) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to get all Products for the specfied Dataset.");
        }

        Set<DatasetProductDTO> productsToRet = securityService.getProductsByDataset(dataset);
        return new ResponseEntity<>(productsToRet, HttpStatus.OK);
    }

}
