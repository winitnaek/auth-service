package com.bsi.sec.saml;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bsi.sec.saml.exception.UnknownUserException;

/**
 * All assertion properties and attributes are held in the attributes map.
 * Capture all application, provider specific logic in the concrete implementations of this interface.
 * 
 * @author SudhirP
 *
 */
public interface LoginCallback {		
	boolean allowUserAccess(Map<String, String> attributes, HttpServletRequest request, HttpServletResponse response) throws UnknownUserException;
}
