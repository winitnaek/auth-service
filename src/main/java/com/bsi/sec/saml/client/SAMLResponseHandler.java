/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.saml.client;

import com.bsi.sec.dao.SSOConfigurationDao;
import com.bsi.sec.dao.TenantDao;
import com.bsi.sec.domain.SSOConfiguration;
import com.bsi.sec.dto.SSOAction;
import com.bsi.sec.dto.SSOResult;
import com.bsi.sec.exception.InvalidUserException;
import com.bsi.sec.saml.SAMLHelper;
import com.bsi.sec.saml.exception.ConfigurationException;
import com.bsi.sec.saml.exception.InvalidIssuerException;
import com.bsi.sec.saml.exception.InvalidResponseException;
import com.bsi.sec.saml.exception.InvalidSignatureException;
import com.bsi.sec.saml.exception.SamlDecodeException;
import com.bsi.sec.svc.AuditLogger;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.opensaml.core.config.InitializationException;
import org.opensaml.saml.saml2.core.LogoutResponse;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.saml.saml2.core.StatusResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author SudhirP
 */
@Service
public class SAMLResponseHandler {

    private static final Logger log = LoggerFactory.getLogger(SAMLResponseHandler.class);

    @Autowired
    private SSOConfigurationDao ssoConfDao;
    

    @Autowired
    private AuditLogger auditLogger;

    /**
     * Initialize.
     *
     * @throws ConfigurationException
     */
    public static void initialize() throws ConfigurationException {
        //
        try {
            org.opensaml.core.config.InitializationService.initialize();
        } catch (InitializationException ex) {
            throw new ConfigurationException("Unable to bootstrap Open SAML library! ", ex);
        }
    }

    public SSOResult processResponse(String saml, String relayState) throws SamlDecodeException, InvalidIssuerException, InvalidSignatureException, InvalidResponseException, CertificateException {
        SSOResult result = new SSOResult();
        result.setAction(SSOAction.LOGIN);
        //
        StatusResponseType response = (StatusResponseType) SAMLHelper.decodeResponse(saml);
        String issuer = SAMLHelper.getResponseIssuer(response);
        //
        if (log.isDebugEnabled()) {
            log.debug("Response: " + SAMLHelper.xmlObjectToString(response));
        }
        if (StringUtils.isEmpty(issuer)) {
            throw new InvalidIssuerException("Unknown Issuer: " + issuer + " Issuer cannot be empty!");
        }
        if (log.isInfoEnabled()) {
            log.info("Handling response from issuer" + issuer);
            log.info("Validating issuer: " + issuer + " against configured idp issuer...");
        }
        List<SSOConfiguration> ssoConfigs = ssoConfDao.getSSOConfByIssuer(issuer);
        if (ssoConfigs == null || ssoConfigs.size()==0) {
            throw new InvalidIssuerException("No valid configuration. Unknown Issuer: " + issuer);
        }        
        //
        if (response instanceof Response) {
            //Map<String, String> attributes = ResponseFactory.getResponse(ssoConfig.isValidateRespSignature(), configuredCert).processAuthenticationResponse((Response) response);
            Map<String, String> attributes = ResponseFactory.getResponse(false, null).processAuthenticationResponse((Response) response);
            //
            String uuid = getUUID(attributes);
            if(StringUtils.isEmpty(uuid))
                throw new InvalidUserException("Invalid SAML Response, No UUID!");
            //
            SSOConfiguration applicableConf = null;
            for(SSOConfiguration conf : ssoConfigs)
            {
                if(conf.getTenant().getDataset().equalsIgnoreCase(uuid))
                {
                    applicableConf = conf;
                    break;
                }
            }
            if(applicableConf == null)
                 throw new InvalidUserException("Invalid User, dataset/tenant not configured for SSO!");
            //
            Certificate configuredCert = null;
            if (applicableConf.isValidateRespSignature()) {
                configuredCert = getCertificate(applicableConf.getCertText());
                if(!SAMLHelper.hasValidResponseSignature(response, configuredCert))
                    throw new InvalidSignatureException("Invalid Signature In Authentication Response! \n" + SAMLHelper.xmlObjectToString(response));
            }
            //
            result.setAttributes(attributes);
        } else if (response instanceof LogoutResponse) {
            result.setAction(SSOAction.LOGOUT);
        } else {
            throw new InvalidUserException("Invalid SAML Response!");
        }
        //
        auditLogger.logAccess(null,
                AuditLogger.Areas.SSO,
                (SSOAction.LOGIN.equals(result.getAction())
                ? AuditLogger.Ops.LOGIN : AuditLogger.Ops.LOGOUT),
                result.getAttributes());
        return result;
    }

    private Certificate getCertificate(String certText) throws CertificateException {
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        InputStream is = new ByteArrayInputStream(certText.getBytes());
        X509Certificate cer = (X509Certificate) fact.generateCertificate(is);
        return cer;
    }
    
    private String getUUID(Map<String, String> attrs)
    {
        Set<Map.Entry<String, String>> entries = attrs.entrySet();
        for(Entry<String,String> e : entries)
        {
           if(e.getKey().equalsIgnoreCase("companyuuid"))
               return e.getValue();
        }
        return null;
    }

}
