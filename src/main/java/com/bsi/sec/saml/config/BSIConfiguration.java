package com.bsi.sec.saml.config;

import javax.xml.bind.annotation.*;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/** Configuration for the SAML client.
 * 
 * @author SudhirP
 *
 */

@XmlType(propOrder = {"id", "idpIssuer","idpReqURL","spConsumerURL","spIssuer","attribIndex","validateRespSignature","validateIdpIssuer","signRequests",
		"allowLogout", "nonSamlLogoutURL","redirectToApplication","appRedirectURL","loginCallbackClass","logoutCallbackClass",
		"keyStorePath","keyStorePassword","certAlias","certPassword","expireRequestSecs"})
@XmlRootElement
public class BSIConfiguration{				
	private int id;
	private String idpIssuer; //idp issuer url.
	private String idpReqURL; //for sp initiated requests.
	private String spConsumerURL; //sp consumer url
	private String spIssuer; //sp/rp issuer property 
	private String attribIndex;
	//flags
	private boolean validateRespSignature;
	private boolean validateIdpIssuer;	
	//
	private boolean allowLogout;
	private String nonSamlLogoutURL;
	//
	private boolean redirectToApplication;
	private String appRedirectURL;
	//class implemented by applications if not redirecting to application
	private String loginCallbackClass;
	private String logoutCallbackClass;
	//sign requests & related configuration - keystore, cert and credentials.
	private boolean signRequests;
	private String keyStorePath;
	private String keyStorePassword;
	private String certAlias;
	private String certPassword;
	//	
	private String expireRequestSecs;
	
	public BSIConfiguration(){}
	
	
	/**
	 * @return the nonSamlLogoutURL
	 */
	public String getNonSamlLogoutURL() {
		return nonSamlLogoutURL;
	}


	/**
	 * @param nonSamlLogoutURL the nonSamlLogoutURL to set
	 */
	public void setNonSamlLogoutURL(String nonSamlLogoutURL) {
		this.nonSamlLogoutURL = nonSamlLogoutURL;
	}


	/**
	 * @return the id
	 */
	@XmlElement(required=true)
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the idpIssuer
	 */
	@XmlElement(required=true)
	public String getIdpIssuer() {
		return idpIssuer;
	}
	/**
	 * @param idpIssuer the idpIssuer to set
	 */
	public void setIdpIssuer(String idpIssuer) {
		this.idpIssuer = idpIssuer;
	}
	/**
	 * @return the consumerURL
	 */
	@XmlElement(required=true)
	public String getSpConsumerURL() {
		return spConsumerURL;
	}
	/**
	 * @param consumerURL the consumerURL to set
	 */
	public void setSpConsumerURL(String consumerURL) {
		this.spConsumerURL = consumerURL;
	}
	/**
	 * @return the spIssuer
	 */
	@XmlElement(required=true)
	public String getSpIssuer() {
		return spIssuer;
	}
	/**
	 * @param spIssuer the spIssuer to set
	 */
	public void setSpIssuer(String spIssuer) {
		this.spIssuer = spIssuer;
	}
	/**
	 * @return the attribIndex
	 */
	public String getAttribIndex() {
		return attribIndex;
	}
	/**
	 * @param attribIndex the attribIndex to set
	 */
	public void setAttribIndex(String attribIndex) {
		this.attribIndex = attribIndex;
	}
	/**
	 * @return the validateRespSignature
	 */
	@XmlElement(required=true)
	public boolean isValidateRespSignature() {
		return validateRespSignature;
	}
	/**
	 * @param validateRespSignature the validateRespSignature to set
	 */
	public void setValidateRespSignature(boolean validateRespSignature) {
		this.validateRespSignature = validateRespSignature;
	}
	/**
	 * @return the validateIdpIssuer
	 */
	@XmlElement(required=true)
	public boolean isValidateIdpIssuer() {
		return validateIdpIssuer;
	}
	/**
	 * @param validateIdpIssuer the validateIdpIssuer to set
	 */
	public void setValidateIdpIssuer(boolean validateIdpIssuer) {
		this.validateIdpIssuer = validateIdpIssuer;
	}
	/**
	 * @return the signRequests
	 */
	@XmlElement(required=true)
	public boolean isSignRequests() {
		return signRequests;
	}
	/**
	 * @param signRequests the signRequests to set
	 */
	public void setSignRequests(boolean signRequests) {
		this.signRequests = signRequests;
	}
	
	/**
	 * @return the allowLogout
	 */
	@XmlElement(required=true)
	public boolean isAllowLogout() {
		return allowLogout;
	}
	/**
	 * @param allowLogout the allowLogout to set
	 */
	public void setAllowLogout(boolean allowLogout) {
		this.allowLogout = allowLogout;
	}
	/**
	 * @return the redirectToApplication
	 */
	public boolean isRedirectToApplication() {
		return redirectToApplication;
	}
	/**
	 * @param redirectToApplication the redirectToApplication to set
	 */
	public void setRedirectToApplication(boolean redirectToApplication) {
		this.redirectToApplication = redirectToApplication;
	}
	/**
	 * @return the appRedirectURL
	 */
	public String getAppRedirectURL() {
		return appRedirectURL;
	}
	/**
	 * @param appRedirectURL the appRedirectURL to set
	 */
	public void setAppRedirectURL(String appRedirectURL) {
		this.appRedirectURL = appRedirectURL;
	}
	/**
	 * @return the loginCallbackClass
	 */
	public String getLoginCallbackClass() {
		return loginCallbackClass;
	}
	/**
	 * @param loginCallbackClass the loginCallbackClass to set
	 */
	public void setLoginCallbackClass(String loginCallbackClass) {
		this.loginCallbackClass = loginCallbackClass;
	}
	/**
	 * @return the logoutCallbackClass
	 */
	public String getLogoutCallbackClass() {
		return logoutCallbackClass;
	}
	/**
	 * @param logoutCallbackClass the logoutCallbackClass to set
	 */
	public void setLogoutCallbackClass(String logoutCallbackClass) {
		this.logoutCallbackClass = logoutCallbackClass;
	}
	/**
	 * @return the keyStorePath
	 */
	public String getKeyStorePath() {
		return keyStorePath;
	}
	/**
	 * @param keyStorePath the keyStorePath to set
	 */
	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}
	/**
	 * @return the keyStorePassword
	 */
	public String getKeyStorePassword() {
		return keyStorePassword;
	}
	/**
	 * @param keyStorePassword the keyStorePassword to set
	 */
	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}
	/**
	 * @return the certAlias
	 */
	public String getCertAlias() {
		return certAlias;
	}
	/**
	 * @param certAlias the certAlias to set
	 */
	public void setCertAlias(String certAlias) {
		this.certAlias = certAlias;
	}
	/**
	 * @return the certPassword
	 */
	public String getCertPassword() {
		return certPassword;
	}
	/**
	 * @param certPassword the certPassword to set
	 */
	public void setCertPassword(String certPassword) {
		this.certPassword = certPassword;
	}
	/**
	 * @return the idpReqURL
	 */
	@XmlElement(required=true)
	public String getIdpReqURL() {
		return idpReqURL;
	}
	/**
	 * @param idpReqURL the idpReqURL to set
	 */
	public void setIdpReqURL(String idpReqURL) {
		this.idpReqURL = idpReqURL;
	}
	
	
	/**
	 * @return the expireRequestSecs
	 */
	public String getExpireRequestSecs() {
		return expireRequestSecs;
	}
	/**
	 * @param expireRequestSecs the expireRequestSecs to set
	 */
	public void setExpireRequestSecs(String expireRequestSecs) {
		this.expireRequestSecs = expireRequestSecs;
	}


	

	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Configuration [id=" + id + ", idpIssuer=" + idpIssuer
				+ ", idpReqURL=" + idpReqURL + ", spConsumerURL="
				+ spConsumerURL + ", spIssuer=" + spIssuer + ", attribIndex="
				+ attribIndex + ", validateRespSignature="
				+ validateRespSignature + ", validateIdpIssuer="
				+ validateIdpIssuer + ", allowLogout=" + allowLogout
				+ ", nonSamlLogoutURL=" + nonSamlLogoutURL
				+ ", redirectToApplication=" + redirectToApplication
				+ ", appRedirectURL=" + appRedirectURL
				+ ", loginCallbackClass=" + loginCallbackClass
				+ ", logoutCallbackClass=" + logoutCallbackClass
				+ ", signRequests=" + signRequests + ", keyStorePath="
				+ keyStorePath + ", keyStorePassword=" + keyStorePassword
				+ ", certAlias=" + certAlias + ", certPassword=" + certPassword
				+ ", expireRequestSecs=" + expireRequestSecs + "]";
	}


	public String toHtmlString() {
		return "<b><h3>Configuration id=" + id + "</h3></b> idpIssuer=" + idpIssuer
				+ "<br> idpReqURL=" + idpReqURL + "<br> spConsumerURL="
				+ spConsumerURL + "<br> spIssuer=" + spIssuer + "<br> attribIndex="
				+ attribIndex + "<br> validateRespSignature="
				+ validateRespSignature + "<br> validateIdpIssuer="
				+ validateIdpIssuer + "<br> allowLogout=" + allowLogout
				+ "<br> nonSamlLogoutURL=" + nonSamlLogoutURL
				+ "<br> redirectToApplication=" + redirectToApplication
				+ "<br> appRedirectURL=" + appRedirectURL
				+ "<br> loginCallbackClass=" + loginCallbackClass
				+ "<br> logoutCallbackClass=" + logoutCallbackClass
				+ "<br> signRequests=" + signRequests + "<br> keyStorePath="
				+ keyStorePath + "<br> keyStorePassword=" + keyStorePassword
				+ "<br> certAlias=" + certAlias + "<br> certPassword=" + certPassword
				+ "<br> expireRequestSecs=" + expireRequestSecs ;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this.id).append(this.idpIssuer).toHashCode();
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BSIConfiguration other = (BSIConfiguration) obj;
		return new EqualsBuilder().append(this.id, other.id).append(this.idpIssuer, other.idpIssuer).isEquals();
	}
}
