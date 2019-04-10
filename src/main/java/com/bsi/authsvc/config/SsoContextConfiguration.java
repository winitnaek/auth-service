/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.authsvc.config;

import static com.bsi.authsvc.util.WSConstants.DEPLOY_SSO_SERVICE;
import static com.bsi.authsvc.util.WSConstants.ENDPOINT_SSO;
import static com.bsi.authsvc.util.WSConstants.PROP_PREFIX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *
 *
 * @authors Igor, Sudhir
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.bsi.authsvc.ws.sso"})
@SuppressWarnings("unchecked")
public class SsoContextConfiguration implements WebMvcConfigurer {

    @Autowired
    private DispatcherServlet dispServlet;

    @Bean
    @SuppressWarnings("rawtypes")
    public ServletRegistrationBean ssoServletBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(
                dispServlet, "/sso/*");
        bean.setName("ssoServlet");
        bean.setLoadOnStartup(1);
        return bean;
    }

    @Bean(name = ENDPOINT_SSO)
    @ConditionalOnProperty(name = DEPLOY_SSO_SERVICE, prefix = PROP_PREFIX)
    public Integer ssoEndpoint() throws Exception {
        return 0;
    }

}
