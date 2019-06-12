/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.config;

import com.bsi.sec.exception.InvalidUserException;
import static com.bsi.sec.util.CacheConstants.SEC_CACHE;
import com.bsi.sec.util.LogUtils;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 *
 * Supports LDAP authentication.
 *
 * @author igorV
 */
@Component
public class LdapAuthenticationManager {

    private static final Logger log = LoggerFactory.getLogger(LdapAuthenticationManager.class);

    private static final SearchControls searchCtls;

    @Autowired
    private SecurityServiceProperties props;

    static {
        searchCtls = new SearchControls();
        final String returnedAtts[] = {"cn"};
        searchCtls.setReturningAttributes(returnedAtts);
        searchCtls.setSearchScope(SearchControls.OBJECT_SCOPE);
        searchCtls.setCountLimit(1);
    }
    public static final int ERR_CODE_AUTH = 100;

    @Cacheable(cacheNames = SEC_CACHE, key = "#userName")
    @SuppressWarnings("UseSpecificCatch")
    public boolean isValidUser(String userName, String password) throws Exception {
        DirContext ctx = null;
        try {
            String url = props.getLdap().getUrl();
            String dnSuffix = props.getLdap().getUserDNSuffix();
            String roleCN = props.getLdap().getGroupCN();
            ctx = getContext(url, dnSuffix, userName, password);
            return hasRole(ctx, roleCN, userName);
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(LogUtils.jsonize("msg", ERR_CODE_AUTH
                        + ": Unable to authenticate/authorize user: "
                        + userName + ". Error msg: " + e.getMessage()), e);
            }

            throw new InvalidUserException("Invalid credentials!!", e);
        } finally {
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (NamingException ex) {
                    if (log.isWarnEnabled()) {
                        log.warn(LogUtils.jsonize(
                                "msg", "Unable to close dir ctx!"), ex);
                    }
                }
            }
        }
    }

    private DirContext getContext(final String url, final String dnSuffix, final String userName, final String password) throws NamingException {
        @SuppressWarnings("UseOfObsoleteCollectionType")
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, getFullDN(userName, dnSuffix));
        env.put(Context.SECURITY_CREDENTIALS, password);
        return new InitialDirContext(env);
    }

    private String getFullDN(String userName, String dnSuffix) {
        if (dnSuffix.startsWith(",")) {
            return "uid=" + userName + dnSuffix;
        } else {
            return "uid=" + userName + "," + dnSuffix;
        }
    }

    private boolean hasRole(DirContext ctx, final String roleCn, final String userName) throws NamingException {
        String searchFilter = props.getLdap().getGroupSearchFilter();
        //
        NamingEnumeration<SearchResult> ne = ctx.search(roleCn, searchFilter, new Object[]{userName}, searchCtls);

        if (ne.hasMore()) {
            if (log.isDebugEnabled()) {
                SearchResult sr = ne.next();
                if (log.isDebugEnabled()) {
                    log.debug(sr.toString());
                }
            }
            return true;
        }
        return false;
    }
}
