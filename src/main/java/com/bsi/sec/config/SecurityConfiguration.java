/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.config;

import com.bsi.sec.util.LogUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * Spring security configuration, applicable only to REST tier.
 *
 * @author SudhirP
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${management.endpoints.web.base-path}")
    private String mgmntEndpWebBsePath;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(mgmntEndpWebBsePath + "/**").denyAll(); // deny actuator access at root level!
        http.csrf().disable();
        http.cors().disable();
    }

    @Bean
    public AuthenticationEntryPoint authFailureEntryPoint() {
        return new AuthenticationEntryPoint() {

            private final Logger log = LoggerFactory.getLogger(this.getClass());

            @Override
            public void commence(HttpServletRequest hsr, HttpServletResponse hsr1, org.springframework.security.core.AuthenticationException ae) throws IOException, ServletException {
                if (log.isInfoEnabled()) {
                    log.info(LogUtils.jsonize("Called Pre-Authentication entry point! Access is rejected for [{}]!",
                            hsr.getRequestURL().toString()));
                }
                hsr1.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid Credentials! Access is denied!");
            }
        };
    }

}
