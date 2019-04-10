/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.authsvc.config;

import static com.bsi.authsvc.util.WSConstants.DEPLOY_AUTH_SERVICE;
import static com.bsi.authsvc.util.WSConstants.DEPLOY_ECHO_SERVICE;
import static com.bsi.authsvc.util.WSConstants.ENDPOINT_ECHO;
import static com.bsi.authsvc.util.WSConstants.ENDPOINT_AUTH;
import static com.bsi.authsvc.util.WSConstants.PROP_PREFIX;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.WebApplicationContext;

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
@ComponentScan(basePackages = {"com.bsi.authsvc.ws.login"})
@SuppressWarnings("unchecked")
public class LoginContextConfiguration implements WebMvcConfigurer {

    @Autowired
    private WebApplicationContext appContext;

    @Bean
    public MappingJackson2HttpMessageConverter jsonConverter() {
        MappingJackson2HttpMessageConverter jsonMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = jsonMessageConverter.getObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return jsonMessageConverter;
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, jsonConverter());
    }

    @Bean
    public DispatcherServlet getDispatcherServlet() {
        DispatcherServlet dispatcher = new DispatcherServlet(appContext);
        return dispatcher;
    }

    @Bean
    @SuppressWarnings("rawtypes")
    public ServletRegistrationBean loginServletBean() {
        ServletRegistrationBean bean = new ServletRegistrationBean(
                getDispatcherServlet(), "/r/*");
        bean.setName("loginServlet");
        bean.setLoadOnStartup(1);
        return bean;
    }

    @Bean(name = ENDPOINT_ECHO)
    @ConditionalOnProperty(name = DEPLOY_ECHO_SERVICE, prefix = PROP_PREFIX)
    public Integer echoEndpoint() throws Exception {
        return 0;
    }

    @Bean(name = ENDPOINT_AUTH)
    @ConditionalOnProperty(name = DEPLOY_AUTH_SERVICE, prefix = PROP_PREFIX)
    public Integer authEndpoint() throws Exception {
        return 0;
    }

}
