/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.authsvc.util;

/**
 *
 * @author igorV
 */
public final class WSConstants {

    public static final String SVC_VERSION = "v1";
    //
    //
    public static final String REST_PREFIX = "/r";
    public static final String ECHO_SERVICE = "/" + SVC_VERSION + "/EchoService";
    public static final String AUTH_SERVICE = "/" + SVC_VERSION + "/AuthService";
    public static final String SSO_SERVICE = "/" + SVC_VERSION + "/SSOService";
    
    //
    public static final String PROP_PREFIX = "authws";
    public static final String DEPLOY_ECHO_SERVICE = "echo.service.deploy";
    public static final String DEPLOY_AUTH_SERVICE = "auth.service.deploy";
    public static final String DEPLOY_SSO_SERVICE = "sso.service.deploy";

    public static final String ENDPOINT_ECHO = "echoEndpoint";
    public static final String ENDPOINT_AUTH = "authEndpoint";
    public static final String ENDPOINT_SSO = "ssoEndpoint";
}
