/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.bsi.sec.base.BaseTest;
import com.bsi.sec.dto.SSOConfigDTO;
import com.bsi.sec.startup.ApplicationInitializer;
import com.bsi.sec.svc.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author igorV
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationInitializer.class})
@WithMockUser(username = "user", password = "password")
@TestPropertySource("classpath:test.properties")
public class SecurityServiceResourceTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(SecurityServiceResourceTest.class);

    @Autowired
    private SecurityServiceResource secServiceResource;

    private MockMvc mockmvc;

    @MockBean
    private SecurityService securityService;

    @Before
    public void setup() {
        this.mockmvc = MockMvcBuilders.standaloneSetup(secServiceResource).build();
    }

    @Test
    public void testGetProductsByDataset() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String contentAsJson = mapper.writeValueAsString(datasetname);

        MockHttpServletResponse response = mockmvc
                .perform(get("/v1/SecurityService/getProductsByDataset")
                        .param("datasetName", datasetname)
                        .content(contentAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        log.info("Status -> {}, Response Content -> {}, Error Message -> {}",
                response.getStatus(), response.getContentAsString(), response.getErrorMessage());

    }

    @Test
    public void testRunFullSFSyncSuccess() throws Exception {
        MockHttpServletResponse response = mockmvc
                .perform(post("/v1/SecurityService/runFullSFSync")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        log.info("Status -> {}, Response Content -> {}, Error Message -> {}",
                response.getStatus(), response.getContentAsString(), response.getErrorMessage());
    }

    @Test
    public void testRunPerSFSyncSuccess() throws Exception {
        String fromDateTimeAsString = DateTimeFormatter.ISO_DATE_TIME
                .withZone(ZoneOffset.UTC)
                .format(LocalDateTime.now(ZoneOffset.UTC));
        MockHttpServletResponse response = mockmvc
                .perform(post("/v1/SecurityService/runSFSync")
                        .param("fromDateTime", fromDateTimeAsString))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        log.info("Status -> {}, Response Content -> {}, Error Message -> {}",
                response.getStatus(), response.getContentAsString(), response.getErrorMessage());
    }

    @Test
    public void testRunPerTPFSyncSuccess() throws Exception {
        String fromDateTimeAsString = DateTimeFormatter.ISO_DATE_TIME
                .withZone(ZoneOffset.UTC)
                .format(LocalDateTime.now(ZoneOffset.UTC));
        MockHttpServletResponse response = mockmvc
                .perform(post("/v1/SecurityService/runTPFSync")
                        .param("fromDateTime", fromDateTimeAsString))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        log.info("Status -> {}, Response Content -> {}, Error Message -> {}",
                response.getStatus(), response.getContentAsString(), response.getErrorMessage());
    }

    @Test
    public void testEnableSFSyncSuccess() throws Exception {
        MockHttpServletResponse response = mockmvc
                .perform(post("/v1/SecurityService/enableSFSync")
                        .param("enabled", Boolean.TRUE.toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        log.info("Status -> {}, Response Content -> {}, Error Message -> {}",
                response.getStatus(), response.getContentAsString(), response.getErrorMessage());
    }

    @Test
    public void testEnableTPFSyncSuccess() throws Exception {
        MockHttpServletResponse response = mockmvc
                .perform(post("/v1/SecurityService/enableTPFSync")
                        .param("enabled", Boolean.TRUE.toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        log.info("Status -> {}, Response Content -> {}, Error Message -> {}",
                response.getStatus(), response.getContentAsString(), response.getErrorMessage());
    }

    @Test
    public void testAddTenant() throws Exception {
        MockHttpServletResponse response = mockmvc
                .perform(post("/v1/SecurityService/addTenant")
                        .param("accountName", acctname)
                        .param("productName", prodname)
                        .param("datasetName", datasetname))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        log.info("Status -> {}, Response Content -> {}, Error Message -> {}",
                response.getStatus(), response.getContentAsString(), response.getErrorMessage());
    }

    @Test
    public void testDeleteTenant() throws Exception {
        MockHttpServletResponse response = mockmvc
                .perform(post("/v1/SecurityService/deleteTenant")
                        .param("id", id.toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        log.info("Status -> {}, Response Content -> {}, Error Message -> {}",
                response.getStatus(), response.getContentAsString(), response.getErrorMessage());
    }

    @Test
    public void testAddSSOConfig() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SSOConfigDTO ssoConfigIn = populateSSOConfig();
        String ssoConfigAsJson = mapper.writeValueAsString(ssoConfigIn);

        MockHttpServletResponse response = mockmvc
                .perform(post("/v1/SecurityService/addSSOConfig")
                        .content(ssoConfigAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        log.info("Status -> {}, Response Content -> {}, Error Message -> {}",
                response.getStatus(), response.getContentAsString(), response.getErrorMessage());
    }
    
    @Test
    public void testUpdateSSOConfig() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SSOConfigDTO ssoConfigIn = populateSSOConfig();
        String ssoConfigAsJson = mapper.writeValueAsString(ssoConfigIn);

        MockHttpServletResponse response = mockmvc
                .perform(post("/v1/SecurityService/updateSSOConfig")
                        .content(ssoConfigAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        log.info("Status -> {}, Response Content -> {}, Error Message -> {}",
                response.getStatus(), response.getContentAsString(), response.getErrorMessage());
    }

    private SSOConfigDTO populateSSOConfig() {
        SSOConfigDTO ssoConfigIn = new SSOConfigDTO();
        ssoConfigIn.setAcctName(acctname);
        ssoConfigIn.setDsplName(acctname + " SSO Configuration 1");
        ssoConfigIn.setAllowLogout(true);
        ssoConfigIn.setAppRedirectURL("test");
        ssoConfigIn.setAttribIndex(1);
        ssoConfigIn.setCertAlias("test");
        ssoConfigIn.setCertPassword("test");
        ssoConfigIn.setCertText("test");
        ssoConfigIn.setExpireRequestSecs(120);
        ssoConfigIn.setId(1L);
        ssoConfigIn.setIdpIssuer("test");
        ssoConfigIn.setIdpReqURL("test");
        ssoConfigIn.setNonSamlLogoutURL("test");
        ssoConfigIn.setRedirectToApplication(true);
        ssoConfigIn.setSignRequests(false);
        ssoConfigIn.setSpConsumerURL("test");
        ssoConfigIn.setSpIssuer("test");
        ssoConfigIn.setValidateIdpIssuer(false);
        ssoConfigIn.setValidateRespSignature(false);
        ssoConfigIn.setEnabled(true);
        return ssoConfigIn;
    }
}