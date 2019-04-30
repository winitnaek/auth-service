/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.bsi.sec.base.BaseTest;
import com.bsi.sec.startup.ApplicationInitializer;
import com.bsi.sec.svc.SecurityService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public void testGetRecordsSuccess() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String contentAsJson = mapper.writeValueAsString(datasetname);

        MockHttpServletResponse response = mockmvc
                .perform(get("/v1/SecurityService/getProductsByDataset")
                        .param("dataset", datasetname)
                        .content(contentAsJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        log.info("Status -> {}, Error Message -> {}", response.getStatus(), response.getContentAsString());

    }
}
