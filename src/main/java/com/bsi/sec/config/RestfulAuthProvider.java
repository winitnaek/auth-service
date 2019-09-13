/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.config;

import com.bsi.sec.config.SecurityServiceProperties.IntUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

/**
 *
 * @author igorV
 */
@Component
public class RestfulAuthProvider implements AuthenticationProvider {

    @Autowired
    private SecurityServiceProperties props;

    @Autowired
    private LdapAuthenticationManager ldapAuthMgr;

    /**
     * Denotes different ways (i.e. LDAP vs Internal User) to authenticate.
     *
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            String name = authentication.getName();
            String password = authentication.getCredentials().toString();

            IntUser intUserCfg = props.getUser();

            if (isSSOUser(authentication, props)) {
                // SSO User!
                if (intUserCfg.isEnabled() && name.equals(intUserCfg.getName())
                        && password.equals(intUserCfg.getPasswd())) {
                    return new UsernamePasswordAuthenticationToken(
                            name, password);
                } else if (props.getLdap().isEnabled()
                        && ldapAuthMgr.isValidUser(name, password)) {
                    return new UsernamePasswordAuthenticationToken(
                            name, password);
                }
            } else {
                // Mgmt UI User! ==> Always validate against LDAP!
                if (props.getLdap().isEnabled()
                        && ldapAuthMgr.isValidUser(name, password)) {
                    return new UsernamePasswordAuthenticationToken(
                            name, password);
                }
            }

            return null; // Invalid user!!
        } catch (Exception ex) {
            throw new BadCredentialsException(ex.getMessage(), ex);
        }
    }

    /**
     * Denotes supported authentication type.
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    /**
     *
     * @return
     */
    private boolean isSSOUser(Authentication authentication, SecurityServiceProperties props) {
        return authentication.getDetails() instanceof SSOAuthenticationDetails
                || props.isDebugMode() && !props.getLdap().isEnabled();
    }

}
