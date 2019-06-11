/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.config;

import com.bsi.sec.config.SecurityServiceProperties.Mgmtui.IntUser;
import com.bsi.sec.config.SecurityServiceProperties.SSOUser;
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

            SSOUser ssoUserCfg = props.getUser();
            //Internal user 
            IntUser userCfg = props.getMgmtui().getUser();

            if (isSSOUser(authentication) && name.equals(ssoUserCfg.getName())
                    && password.equals(ssoUserCfg.getPasswd())) {
                return new UsernamePasswordAuthenticationToken(
                        name, password);
            } else if (userCfg.isEnabled() && name.equals(userCfg.getName()) && password.equals(userCfg.getPasswd())) {
                return new UsernamePasswordAuthenticationToken(
                        name, password);
            } else if (props.getMgmtui().getLdap().isEnabled() && ldapAuthMgr.isValidUser(name, password)) {
                return new UsernamePasswordAuthenticationToken(
                        name, password);
            } else {
                return null;
            }
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
    private boolean isSSOUser(Authentication authentication) {
        return authentication.getDetails() instanceof SSOAuthenticationDetails;
    }

}
