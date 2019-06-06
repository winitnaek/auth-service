package com.bsi.sec.saml.client;

import java.security.cert.Certificate;

/**
 * Factory class.
 * Replace with DI in the future.
 * 
 * @author SudhirP
 *
 */
public class ResponseFactory {
	
	public static SAMLResponse getResponse(boolean validateSignature, Certificate cert)
	{
		return new SAMLResponseImpl(validateSignature, cert);
	}

}
