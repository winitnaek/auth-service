/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.authsvc.ws.login;

import com.bsi.authsvc.dto.LogoutResponse;
import com.bsi.authsvc.dto.LoginResponse;
import com.bsi.authsvc.svc.AuthenticationService;
import com.bsi.authsvc.util.LogUtils;
import com.bsi.authsvc.ws.SecureResource;
import static com.bsi.authsvc.util.ValidatorMessages.DATASET_EMPTY;
import static com.bsi.authsvc.util.WSConstants.AUTH_SERVICE;
import static com.bsi.authsvc.util.WSConstants.ENDPOINT_AUTH;
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
import org.springframework.web.bind.annotation.PostMapping;
import static com.bsi.authsvc.util.ValidatorMessages.USERID_EMPTY;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author vnaik
 */
@RestController
@Validated
@RequestMapping(AUTH_SERVICE)
@ConditionalOnBean(name = ENDPOINT_AUTH)
public class AuthenticationResource implements SecureResource {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationResource.class);

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
    @PostMapping(path = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> login(
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
        LoginResponse response = new LoginResponse();

        if (log.isInfoEnabled()) {
            log.info(LogUtils.jsonize("Response", "response",
                    response.toString()));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     *
     * @param dataset
     * @param empId
     * @param consent
     * @return
     * @throws Exception
     */
    @PostMapping(path = "/logout",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LogoutResponse> logout(
            @Valid
            @NotEmpty(message = DATASET_EMPTY)
            @Size(max = DATASETID_MAX_LEN, message = DATASET_EXCEEDS_SIZE) String dataset,
            @Valid
            @NotEmpty(message = USERID_EMPTY) String userid)
            throws Exception {
        if (log.isInfoEnabled()) {
            log.info(LogUtils.jsonize("Invoked logout endpoint!",
                    "dataset", dataset, "userid", userid));
        }

        // User validation needs to happen!
        // Logout logic needs to be provided!
        LogoutResponse response = new LogoutResponse();

        if (log.isInfoEnabled()) {
            log.info(LogUtils.jsonize("Response", "response",
                    response.toString()));
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
