/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.saml.client;

import com.bsi.sec.dao.SSOConfigurationDao;
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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Map;
import java.security.cert.X509Certificate;
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
        if (log.isDebugEnabled()) 
            log.debug("Response: " + SAMLHelper.xmlObjectToString(response));         
        if(StringUtils.isEmpty(issuer))
            throw new InvalidIssuerException("Unknown Issuer: " + issuer + " Issuer cannot be empty!");
        if (log.isInfoEnabled()) {
            log.info("Handling response from issuer" + issuer);
            log.info("Validating issuer: " + issuer + " against configured idp issuer...");
        }       
        SSOConfiguration ssoConfig = ssoConfDao.getSSOConfByIssuer(issuer);  
        if (ssoConfig == null || !StringUtils.equalsIgnoreCase(StringUtils.trimToEmpty(issuer), StringUtils.trimToEmpty(ssoConfig.getIdpIssuer()))) {
            throw new InvalidIssuerException("Unknow Issuer: " + issuer);
        }
        Certificate configuredCert=null;
        if(ssoConfig.isValidateRespSignature())        
            configuredCert = getCertificate(ssoConfig.getCertText());
        //
        if(response instanceof Response){
            Map<String,String> attributes = ResponseFactory.getResponse(ssoConfig.isValidateRespSignature(), configuredCert).processAuthenticationResponse((Response) response);
            result.setAttributes(attributes);
        }
        else if(response instanceof LogoutResponse)
        {            
            result.setAction(SSOAction.LOGOUT);            
        }
        else
            throw new InvalidUserException("Invalid SAML Response!");
        //
        return result;
    }

    

    private Certificate getCertificate(String certText) throws CertificateException {
        CertificateFactory fact = CertificateFactory.getInstance("X.509");
        InputStream is = new ByteArrayInputStream( certText.getBytes() );
        X509Certificate cer = (X509Certificate) fact.generateCertificate(is);
        return cer;
    }
    
    
   
}
