/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.web.rest;

import com.bsi.sec.dto.CompanyDTO;
import com.bsi.sec.svc.TPFDataPuller;
import static com.bsi.sec.util.WSConstants.SECURITY_SERVICE;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing Tenant.
 *
 * @author vnaik
 */
@RestController
@RequestMapping(SECURITY_SERVICE)
public class TPFDataPullerResource {

    private final Logger log = LoggerFactory.getLogger(TPFDataPullerResource.class);

    @Autowired
    private TPFDataPuller tpfDataPuller;

    /**
     * Retrieves products associated with the given dataset.
     *
     * @param dataset
     * @return
     */
     @GetMapping("/runInitialSync")
    public ResponseEntity<Set<CompanyDTO>> runInitialSync(
            @RequestParam(required = true) String dataset) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to run initial sync for TPF.");
        }
        tpfDataPuller.runInitialSync(null);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
    
     @GetMapping("/runPeriodicSync")
    public ResponseEntity<Set<CompanyDTO>> runPeriodicSync(
            @RequestParam(required = true) String dataset) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("REST request to run periodic sync for TPF.");
        }
        tpfDataPuller.runPeriodicSync( LocalDateTime.of(2019, Month.FEBRUARY, 22, 06, 30));
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
