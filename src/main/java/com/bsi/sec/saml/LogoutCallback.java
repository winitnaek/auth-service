package com.bsi.sec.saml;

import javax.servlet.http.HttpServletRequest;

import com.bsi.sec.saml.exception.ConfigurationException;

/**
 * Implement this interface to customize logout request response.
 * Logout has to be sp initiated. 
 * 
 * @author SudhirP
 *
 */
public interface LogoutCallback {
	boolean isUserLoggedIn(String userId);
	String beforeLogoutRequest(HttpServletRequest request, String userId) throws ConfigurationException;//generate and return a request id, used to match request and response.
	void afterLogoutResponse(String userId); //called when logout response is received.
}
