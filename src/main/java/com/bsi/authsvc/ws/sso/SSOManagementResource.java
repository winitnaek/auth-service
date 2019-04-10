/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.authsvc.ws.sso;

import com.bsi.authsvc.dto.SSOResponse;
import com.bsi.authsvc.svc.AuthenticationService;
import com.bsi.authsvc.util.LogUtils;
import com.bsi.authsvc.ws.SecureResource;
import static com.bsi.authsvc.util.ValidatorMessages.DATASET_EMPTY;
import static com.bsi.authsvc.util.ValidatorConstants.DATASETID_MAX_LEN;
import static com.bsi.authsvc.util.ValidatorMessages.DATASET_EXCEEDS_SIZE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotEmpty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static com.bsi.authsvc.util.ValidatorMessages.USERID_EMPTY;
import static com.bsi.authsvc.util.WSConstants.ENDPOINT_SSO;
import static com.bsi.authsvc.util.WSConstants.SSO_SERVICE;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author vnaik
 */
@RestController
@Validated
@RequestMapping(SSO_SERVICE)
@ConditionalOnBean(name = ENDPOINT_SSO)
public class SSOManagementResource implements SecureResource {

    private static final Logger log = LoggerFactory.getLogger(SSOManagementResource.class);

    @Autowired
    private AuthenticationService svc;

    /**
     *
     * @param dataset
     * @param empId
     * @param consent
     * @return
     * @throws Exception
     */
    @GetMapping(path = "/getConfiguration",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SSOResponse> getConfiguration(
            @Valid
            @NotEmpty(message = DATASET_EMPTY)
            @Size(max = DATASETID_MAX_LEN, message = DATASET_EXCEEDS_SIZE) String dataset,
            @Valid
            @NotEmpty(message = USERID_EMPTY) String userid)
            throws Exception {
        if (log.isInfoEnabled()) {
            log.info(LogUtils.jsonize("Invoked login endpoint!",
                    "dataset", dataset, "userid", userid));
        }

        // User validation needs to happen!
        // Login logic needs to be provided!
        SSOResponse response = new SSOResponse();

        if (log.isInfoEnabled()) {
            log.info(LogUtils.jsonize("Response", "response",
                    response.toString()));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
