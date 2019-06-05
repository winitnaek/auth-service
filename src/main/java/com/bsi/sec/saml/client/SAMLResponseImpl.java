package com.bsi.sec.saml.client;

import static com.bsi.sec.saml.Constants.SUBJECT;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.LogoutResponse;
import org.opensaml.saml.saml2.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import com.bsi.sec.saml.SAMLHelper;
import com.bsi.sec.saml.exception.ConfigurationException;
import com.bsi.sec.saml.exception.InvalidIssuerException;
import com.bsi.sec.saml.exception.InvalidResponseException;
import com.bsi.sec.saml.exception.InvalidSignatureException;
import java.security.cert.Certificate;
import org.opensaml.core.xml.XMLObject;

class SAMLResponseImpl implements SAMLResponse {

	private static final Logger log = LoggerFactory.getLogger(SAMLResponseImpl.class);	
	
	private final boolean validateSignature;
        private final Certificate cert;

    SAMLResponseImpl(boolean sigValidationEnabled, Certificate cert) {
        this.validateSignature = sigValidationEnabled;
        this.cert = cert;
    }


	/**
	 * Process a SAML 2.0 authentication response.
	 * @throws InvalidSignatureException 
	 * @throws InvalidIssuerException 
	 * @throws InvalidResponseException 
	 */
	
	public Map<String, String> processAuthenticationResponse(Response response) throws InvalidSignatureException, InvalidIssuerException, InvalidResponseException {
		//validate signature if configured.
		if(validateSignature)
		{			
                    if(!SAMLHelper.hasValidResponseSignature(response, cert))
				throw new InvalidSignatureException("Invalid Signature In Authentication Response! \n" + SAMLHelper.xmlObjectToString(response));	
		}
		//Assumes a single assertion. modify if necessary.
		Map<String, String> results = new HashMap<String, String>();
		Assertion assertion = (Assertion) response.getAssertions().get(0);		
		if (assertion != null) {
			if(!SAMLHelper.checkConditions(assertion))
				throw new InvalidResponseException("SAML Conditions for notBefore OR notOnOrAfter failed!");
			String subject = assertion.getSubject().getNameID().getValue();
			results.put(SUBJECT, subject);
			List<AttributeStatement> attributeStatementList = assertion.getAttributeStatements();
			if(attributeStatementList == null)return results;
			for(AttributeStatement as : attributeStatementList)
			{
				for(Attribute attrib : as.getAttributes())
				{
					if(attrib.getAttributeValues() == null)continue;
					Element value = ((XMLObject) attrib.getAttributeValues().get(0)).getDOM();
					String attribValue = value.getTextContent();
					results.put(attrib.getName(), attribValue);
				}
			}			
		}
		return results;		
	}


	
	
	


	/**
	 * Process a logout response. Optional after a logout request.
	 * logout issue
	 * @throws InvalidIssuerException 
	 * @throws ConfigurationException 
	 * @throws InvalidSignatureException 
	 */
	
	public void processLogoutResponse(LogoutResponse response) throws InvalidIssuerException, ConfigurationException, InvalidSignatureException {		
		//if(config.isValidateRespSignature() && !SAMLHelper.hasValidResponseSignature(response))
			//throw new InvalidSignatureException("Invalid Signature In Logout Response! \n" + SAMLHelper.xmlObjectToString(response));		
	}

}
