/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.config;

import com.bsi.sec.svc.AuditLogger;
import com.bsi.sec.util.LogUtils;
import static com.bsi.sec.util.WSConstants.MGMTUI_DEBUG_LOGIN_FORM_URL;
import static com.bsi.sec.util.WSConstants.MGMTUI_LOGIN_FORM_URL;
import static com.bsi.sec.util.WSConstants.MGMTUI_LOGIN_PROC_URL;
import static com.bsi.sec.util.WSConstants.MGMTUI_LOGOUT_PROC_URL;
import static com.bsi.sec.util.WSConstants.SESS_COOKIE;
import static com.bsi.sec.util.WSConstants.SSO_SVCS_PREFIX;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring security configuration, applicable only to REST tier.
 *
 * @author SudhirP
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final static Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    @Autowired
    private RestfulAuthProvider authProvider;

    @Autowired
    private AuditLogger auditLogger;

    @Autowired
    private SecurityServiceProperties props;

    /**
     * * Configure Http Security
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/r", "/a/**").denyAll()
                .antMatchers("/error").permitAll()
                .antMatchers(getPatternsToMatch()).fullyAuthenticated()
                .and()
                .httpBasic()
                .authenticationEntryPoint(authFailureEntryPoint())
                .authenticationDetailsSource(authenticationDetailsSource())
                .and()
                .authorizeRequests()
                .antMatchers(MGMTUI_LOGIN_FORM_URL,
                        "/**/*.{js,html,css}").permitAll()
                .anyRequest().fullyAuthenticated()
                .and()
                .formLogin()
                .loginPage(MGMTUI_LOGIN_FORM_URL)
                .loginProcessingUrl(MGMTUI_LOGIN_PROC_URL)
                .failureHandler(authenticationFailureHandler())
                .successHandler(authenticationSuccessHandler())
                .and()
                .sessionManagement()
                .invalidSessionUrl(MGMTUI_LOGIN_FORM_URL)
                .and()
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies(SESS_COOKIE)
                .logoutRequestMatcher(
                        new AntPathRequestMatcher(MGMTUI_LOGOUT_PROC_URL))
                .logoutSuccessHandler(logoutSuccessHandler())
                .and()
                .csrf().disable()
                .cors().disable();
    }

    /**
     *
     * @return
     */
    @Bean
    public AuthenticationEntryPoint authFailureEntryPoint() {
        return new AuthenticationEntryPoint() {

            private final Logger log = LoggerFactory.getLogger(this.getClass());

            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response,
                    org.springframework.security.core.AuthenticationException ae) throws IOException, ServletException {
                if (log.isInfoEnabled()) {
                    log.info(LogUtils.jsonize(
                            "msg", "Called Pre-Authentication entry point! Access is rejected for!",
                            "requestURL", request.getRequestURL().toString()));
                }
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid Credentials! Access is denied!");
            }
        };
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

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
        auth.eraseCredentials(false);
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
     *
     * @return
     */
    private AuthenticationDetailsSource<HttpServletRequest, SSOAuthenticationDetails> authenticationDetailsSource() {
        return (HttpServletRequest request) -> new SSOAuthenticationDetails();
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
        return (HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) -> {
            prepareResponse(response, null, HttpStatus.UNAUTHORIZED,
                    exception);
        };
    }

    /**
     * <ul>
     * <li>
     * Prepare response with OK (i.e. 200) status for proper http status code
     * mapping handling.
     * </li>
     * </ul>
     *
     * @return
     */
    private AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, Authentication auth) -> {
            auditLogger.logAccess((String) auth.getPrincipal(),
                    AuditLogger.Areas.MGMT_UI,
                    AuditLogger.Ops.LOGIN, null);
            prepareResponse(response, auth, HttpStatus.OK, null);
        };
    }

    /**
     *
     * @param response
     * @param auth
     */
    private void prepareResponse(HttpServletResponse response,
            Authentication auth, HttpStatus status,
            AuthenticationException error) {
        response.setStatus(status.value());
        Map<String, Object> data = new HashMap<>();

        data.put(
                "timestamp",
                LocalDateTime.now(ZoneOffset.UTC).toString());

        if (auth != null) {
            data.put(
                    "name",
                    auth.getName());
        }

        if (error != null) {
            data.put(
                    "exception",
                    error.getMessage());
        }

        //
        ObjectMapper objectMapper = new ObjectMapper();
        ServletOutputStream respOut = null;

        try {
            respOut = response.getOutputStream();
            respOut.println(objectMapper.writeValueAsString(data));
        } catch (IOException ex) {
            if (log.isErrorEnabled()) {
                log.error(LogUtils.jsonize(
                        "msg", "Logout Handler failed!!",
                        "error", ex.getMessage()
                ), ex);
            }

            if (respOut != null) {
                IOUtils.closeQuietly(respOut);
            }
        }
    }

    /**
     *
     * @return
     */
    private LogoutSuccessHandler logoutSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response,
                Authentication auth) -> {
            auditLogger.logAccess((String) auth.getPrincipal(),
                    AuditLogger.Areas.MGMT_UI,
                    AuditLogger.Ops.LOGOUT, null);
        };
    }

    /**
     *
     * @return
     */
    private String[] getPatternsToMatch() {
        String ssoReq = "/r" + SSO_SVCS_PREFIX + "/**";
        String debugWebapiReq = MGMTUI_DEBUG_LOGIN_FORM_URL + "/**";
        return props.isDebugMode() && !props.getLdap().isEnabled()
                ? ArrayUtils.toArray(ssoReq, debugWebapiReq)
                : ArrayUtils.toArray(ssoReq);
    }

}
