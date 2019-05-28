/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author igorV
 */
public final class SSOConfigDTO {

    @NotNull
    @Min(1L)
    private Long id;

    @NotNull
    private String dsplName;

    @NotNull
    private String idpIssuer;

    @NotNull
    private String idpReqURL;

    private String spConsumerURL;

    @NotNull
    private String spIssuer;

    @NotNull
    @Min(value = 1)
    private Integer attribIndex;

    @NotNull
    private Boolean validateRespSignature;

    @NotNull
    private Boolean validateIdpIssuer;

    @NotNull
    private Boolean allowLogout;

    private String nonSamlLogoutURL;

    @NotNull
    private Boolean redirectToApplication;

    @NotNull
    private String appRedirectURL;

    @NotNull
    private String certAlias;

    @NotNull
    private String certPassword;

    @NotNull
    private String certText;

    @NotNull
    @Min(value = 1)
    private Integer expireRequestSecs;

    @NotNull
    private Boolean enabled;

    private boolean linked;

    private Long acctId;
    
    private String acctName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isLinked() {
        return linked;
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public Long getAcctId() {
        return acctId;
    }

    public void setAcctId(Long acctId) {
        this.acctId = acctId;
    }

    public String getDsplName() {
        return dsplName;
    }

    public void setDsplName(String dsplName) {
        this.dsplName = dsplName;
    }

    public String getIdpIssuer() {
        return idpIssuer;
    }

    public void setIdpIssuer(String idpIssuer) {
        this.idpIssuer = idpIssuer;
    }

    public String getIdpReqURL() {
        return idpReqURL;
    }

    public void setIdpReqURL(String idpReqURL) {
        this.idpReqURL = idpReqURL;
    }

    public String getSpConsumerURL() {
        return spConsumerURL;
    }

    public void setSpConsumerURL(String spConsumerURL) {
        this.spConsumerURL = spConsumerURL;
    }

    public String getSpIssuer() {
        return spIssuer;
    }

    public void setSpIssuer(String spIssuer) {
        this.spIssuer = spIssuer;
    }

    public Integer getAttribIndex() {
        return attribIndex;
    }

    public void setAttribIndex(Integer attribIndex) {
        this.attribIndex = attribIndex;
    }

    public Boolean getValidateRespSignature() {
        return validateRespSignature;
    }

    public void setValidateRespSignature(Boolean validateRespSignature) {
        this.validateRespSignature = validateRespSignature;
    }

    public Boolean getValidateIdpIssuer() {
        return validateIdpIssuer;
    }

    public void setValidateIdpIssuer(Boolean validateIdpIssuer) {
        this.validateIdpIssuer = validateIdpIssuer;
    }
 
    public Boolean getAllowLogout() {
        return allowLogout;
    }

    public void setAllowLogout(Boolean allowLogout) {
        this.allowLogout = allowLogout;
    }

    public String getNonSamlLogoutURL() {
        return nonSamlLogoutURL;
    }

    public void setNonSamlLogoutURL(String nonSamlLogoutURL) {
        this.nonSamlLogoutURL = nonSamlLogoutURL;
    }

    public Boolean getRedirectToApplication() {
        return redirectToApplication;
    }

    public void setRedirectToApplication(Boolean redirectToApplication) {
        this.redirectToApplication = redirectToApplication;
    }

    public String getAppRedirectURL() {
        return appRedirectURL;
    }

    public void setAppRedirectURL(String appRedirectURL) {
        this.appRedirectURL = appRedirectURL;
    }

    public String getCertAlias() {
        return certAlias;
    }

    public void setCertAlias(String certAlias) {
        this.certAlias = certAlias;
    }

    public String getCertPassword() {
        return certPassword;
    }

    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    public String getCertText() {
        return certText;
    }

    public void setCertText(String certText) {
        this.certText = certText;
    }

    public Integer getExpireRequestSecs() {
        return expireRequestSecs;
    }

    public void setExpireRequestSecs(Integer expireRequestSecs) {
        this.expireRequestSecs = expireRequestSecs;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "SSOConfigDTO{" + "id=" + id + ", dsplName=" + dsplName + ", idpIssuer=" + idpIssuer + ", idpReqURL=" + idpReqURL + ", spConsumerURL=" + spConsumerURL + ", spIssuer=" + spIssuer + ", attribIndex=" + attribIndex + ", validateRespSignature=" + validateRespSignature + ", validateIdpIssuer=" + validateIdpIssuer + ", allowLogout=" + allowLogout + ", nonSamlLogoutURL=" + nonSamlLogoutURL + ", redirectToApplication=" + redirectToApplication + ", appRedirectURL=" + appRedirectURL + ", certAlias=" + certAlias + ", certPassword=" + certPassword + ", certText=" + certText + ", expireRequestSecs=" + expireRequestSecs + ", enabled=" + enabled + ", linked=" + linked + ", acctName=" + acctName + '}';
    }

}
