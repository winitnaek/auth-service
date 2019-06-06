/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.saml;

/**
 *
 * @author SudhirP
 */
public interface Constants {
	static final String LIB_NAME = "_SAML2_";
	static final int EXP_INTERVAL = 20;
	static final String SUBJECT = "Subject";
	static final String RSA = "RSA";
	static final String X_509 = "X.509";
	static final String SINGLE_LOGOUT = "Single Logout";
	//
	static final String REQ_LOGIN = "li";
	static final String REQ_LOGOUT = "lo";
	//static final String REQ_LOGOUT_USER = "usr";
	//
	static final String SAML_RESPONSE = "SAMLResponse";
	static final String SAML_REQUEST = "SAMLRequest";
	static final String RELAY_STATE = "RelayState";
	//
	static final String PARAM_KEY="k";
	static final String PARAM_REQUEST_TYPE="rt";
	static final String PARAM_AUTH_KEY = "au";
	//
	static final String CONFIG_FILE_PATH = "saml-sso-config-file-path";
	static final String CONFIG_FILE_NAME = "sso-config";
	static final String CONFIG_FILE_NAME_DEFAULT = "saml-config.xml";
	static final String REQUEST_HANDSHAKE_KEY = "saml-sso-request-handshake-key";
	static final String BAD_REQUEST = "Un-Authorized Request! This attempt has been logged.";
	static final String SAML_ATTRIBUTES = "SAML_ATTRIBUTES";
	static final String SAML_ISSUER = "SAML_Issuer";
	static final String SAML_DESTINATION = "SAML_Destination";
	static final String INTEGRATION_ENABLED = "SAML_ENABLED";	
	
	static final String ON_BEHALF_PARAMS = "ON_BEHALF_PARAMS";
}
