/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.web.rest;

import com.bsi.sec.dto.EchoInput;
import com.bsi.sec.svc.EchoService;
import com.bsi.sec.util.LogUtils;
import com.bsi.sec.web.rest.base.SecureResource;
import static com.bsi.sec.util.WSConstants.ECHO_SERVICE;
import static com.bsi.sec.util.WSConstants.ENDPOINT_ECHO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author vnaik
 */
@RestController
@RequestMapping(ECHO_SERVICE)
@ConditionalOnBean(name = ENDPOINT_ECHO)
public class EchoResource implements SecureResource {

    private static final Logger log = LoggerFactory.getLogger(EchoResource.class);

    @Autowired
    private EchoService svc;

    @RequestMapping("/echo") //get
    public String echo(@RequestParam(value = "name", defaultValue = "HI") String name) {
        if (log.isDebugEnabled()) {
            log.debug("slf4jlog -  say hi with input object called");
        }
        return svc.echo(getUserName() + " : " + name);
    }

    @PostMapping(path = "/echo2", consumes = MediaType.APPLICATION_XML_VALUE) //post
    public String echo(@RequestBody @Valid EchoInput input) {
        if (log.isDebugEnabled()) {
            log.debug("slf4jlog -  say hi with input object called");
        }

        return svc.echo(input.getName());

    }

    @PostMapping(path = "/echo3", consumes = {
        MediaType.APPLICATION_XML_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE}) //post
    public String echoPart(@RequestPart(value = "input", required = true) @Valid EchoInput input, @RequestPart(value = "input2", required = true) @Valid MultipartFile file) {
        if (log.isDebugEnabled()) {
            log.debug(LogUtils.jsonize(
                    "msg", "slf4jlog -  say hi with input object called",
                    "file", file.toString()));

        }
        return svc.echo(input.getName());

    }

    @PostMapping(path = "/echo4", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) //post
    public String echo(@RequestPart(value = "input", required = true) @Valid MultipartFile file) {
        if (log.isDebugEnabled()) {
            log.debug("slf4jlog -  say hi with input object called");
        }
        return svc.echo(file.getName());

    }
}
