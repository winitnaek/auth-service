/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.config;

import com.bsi.sec.util.LogUtils;
import static com.bsi.sec.util.WSConstants.MGMTUI_LOGIN_URL;
import static com.bsi.sec.util.WSConstants.MGMTUI_LOGOUT_URL;
import static com.bsi.sec.util.WSConstants.SESS_COOKIE;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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

    @Autowired
    private MgmtUIRestfulAuthProvider mgmtUIauthProvider;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authMgrBldr) throws Exception {
        ProviderManager authMgr = new ProviderManager(Arrays.asList(mgmtUIauthProvider));
        authMgr.setEraseCredentialsAfterAuthentication(false);
        authMgrBldr.parentAuthenticationManager(authMgr);
    }

    /**
     * * Configure Http Security
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authFailureEntryPoint())
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(mgmntEndpWebBsePath + "/**").denyAll() // deny actuator access at root level!
                .anyRequest().fullyAuthenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .formLogin().loginProcessingUrl(MGMTUI_LOGIN_URL)
                .failureHandler(authenticationFailureHandler())
                .successHandler(authenticationSuccessHandler())
                .and()
                .logout().logoutUrl(MGMTUI_LOGOUT_URL).clearAuthentication(true)
                .deleteCookies(SESS_COOKIE).invalidateHttpSession(true);
    }

    /**
     * Configure Web Security
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                // Allow server capabilities request from browser!
                .antMatchers(HttpMethod.OPTIONS, "/**");
    }

    @Bean
    public AuthenticationEntryPoint authFailureEntryPoint() {
        return new AuthenticationEntryPoint() {

            private final Logger log = LoggerFactory.getLogger(this.getClass());

            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response,
                    org.springframework.security.core.AuthenticationException ae) throws IOException, ServletException {
                if (log.isInfoEnabled()) {
                    log.info(LogUtils.jsonize(null,
                            "msg", "Called Pre-Authentication entry point! Access is rejected for!",
                            "requestURL", request.getRequestURL().toString()));
                }
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid Credentials! Access is denied!");
            }
        };
    }

    /**
     *
     * @return
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * <pre>
     * <ul>
     * <li>
     * Prepare response with UNAUTHORIZED (i.e. 401) status for proper
     * http status code mapping
     * handling.
     * </li>
     * </ul>
     * </pre>
     *
     * @return
     */
    private AuthenticationFailureHandler authenticationFailureHandler() {
        return new AuthenticationFailureHandler() {
            /**
             *
             * @param request
             * @param response
             * @param exception
             * @throws IOException
             * @throws ServletException
             */
            @Override
            public void onAuthenticationFailure(HttpServletRequest request,
                    HttpServletResponse response,
                    AuthenticationException exception)
                    throws IOException, ServletException {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                Map<String, Object> data = new HashMap<>();
                data.put(
                        "timestamp",
                        LocalDateTime.now(ZoneOffset.UTC).toString());
                data.put(
                        "exception",
                        exception.getMessage());
                //
                ObjectMapper objectMapper = new ObjectMapper();
                response.getOutputStream()
                        .println(objectMapper.writeValueAsString(data));
            }
        };
    }

    /**
     * <pre>
     * <ul>
     * <li>
     * Prepare response with OK (i.e. 200) status for proper
     * http status code mapping
     * handling.
     * </li>
     * </ul>
     * </pre>
     *
     * @return
     */
    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            /**
             *
             * @param hsr
             * @param hsr1
             * @param a
             * @throws IOException
             * @throws ServletException
             */
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                    HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
                response.setStatus(HttpStatus.OK.value());
                Map<String, Object> data = new HashMap<>();

                data.put(
                        "timestamp",
                        LocalDateTime.now(ZoneOffset.UTC).toString());
                data.put(
                        "name",
                        authentication.getName());
                //
                ObjectMapper objectMapper = new ObjectMapper();
                response.getOutputStream()
                        .println(objectMapper.writeValueAsString(data));
            }
        };
    }

}
