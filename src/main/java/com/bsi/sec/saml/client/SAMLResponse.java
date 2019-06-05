package com.bsi.sec.saml.client;

import java.util.Map;


import org.opensaml.saml.saml2.core.Response;
import org.opensaml.saml.saml2.core.LogoutResponse;

import com.bsi.sec.saml.exception.ConfigurationException;
import com.bsi.sec.saml.exception.InvalidIssuerException;
import com.bsi.sec.saml.exception.InvalidResponseException;
import com.bsi.sec.saml.exception.InvalidSignatureException;


public interface SAMLResponse {
	Map<String,String> processAuthenticationResponse(Response response) throws InvalidSignatureException, InvalidIssuerException, InvalidResponseException;			
	void processLogoutResponse(LogoutResponse response) throws InvalidIssuerException, ConfigurationException, InvalidSignatureException;	
}
