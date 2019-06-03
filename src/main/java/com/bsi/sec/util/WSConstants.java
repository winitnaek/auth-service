/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.util;

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
    public static final String SECURITY_SERVICE = "/" + SVC_VERSION + "/SecurityService";
    public static final String SSO_SERVICE = "/" + SVC_VERSION + "/SSOService";
    //
    public static final String PROP_PREFIX = "sws";
    public static final String DEPLOY_ECHO_SERVICE = "echo.service.deploy";
    public static final String DEPLOY_SECURITY_SERVICE = "security.service.deploy";

    public static final String ENDPOINT_ECHO = "echoEndpoint";
    public static final String ENDPOINT_SECURITY_SERVICE = "securityServiceEndpoint";

    public static final String MGMTUI_LOGIN_URL = REST_PREFIX + SECURITY_SERVICE + "/mgmtui_login";
    public static final String MGMTUI_LOGOUT_URL = REST_PREFIX + SECURITY_SERVICE + "/mgmtui_logout";

    public static final String SESS_COOKIE = "JSESSIONID";
}
