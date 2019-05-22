package com.bsi.sec.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Objects;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.hibernate.envers.Audited;

/**
 * A SSOConfiguration.
 */
@Entity
@Audited
@Table(name = "sso_configuration")
public class SSOConfiguration extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @QuerySqlField(index = true)
    private Long id;

    @NotNull
    @Column(name = "dspl_name", nullable = false)
    private String dsplName;

    @NotNull
    @Column(name = "idp_issuer", nullable = false)
    private String idpIssuer;

    @NotNull
    @Column(name = "idp_req_url", nullable = false)
    private String idpReqURL;

    @NotNull
    @Column(name = "sp_consumer_url", nullable = false)
    private String spConsumerURL;

    @NotNull
    @Column(name = "sp_issuer", nullable = false)
    private String spIssuer;

    @NotNull
    @Min(value = 1)
    @Column(name = "attrib_index", nullable = false)
    private Integer attribIndex;

    @NotNull
    @Column(name = "validate_resp_signature", nullable = false)
    private Boolean validateRespSignature;

    @NotNull
    @Column(name = "validate_idp_issuer", nullable = false)
    private Boolean validateIdpIssuer;

    @NotNull
    @Column(name = "sign_requests", nullable = false)
    private Boolean signRequests;

    @NotNull
    @Column(name = "allow_logout", nullable = false)
    private Boolean allowLogout;

    @NotNull
    @Column(name = "non_saml_logout_url", nullable = false)
    private String nonSamlLogoutURL;

    @NotNull
    @Column(name = "redirect_to_application", nullable = false)
    private Boolean redirectToApplication;

    @NotNull
    @Column(name = "app_redirect_url", nullable = false)
    private String appRedirectURL;

    @NotNull
    @Column(name = "cert_alias", nullable = false)
    private String certAlias;

    @NotNull
    @Column(name = "cert_password", nullable = false)
    private String certPassword;

    @NotNull
    @Column(name = "cert_text", nullable = false)
    private String certText;

    @NotNull
    @Min(value = 1)
    @Column(name = "expire_request_secs", nullable = false)
    private Integer expireRequestSecs;

    @NotNull
    @Column(name = "enabled", nullable = false)
    @QuerySqlField(name = "enabled")
    private Boolean enabled;

    @NotNull
    @Column(name = "enabled")
    @QuerySqlField(name = "enabled")
    private boolean linked;

    @OneToOne(mappedBy = "ssoConfig")
    @JsonIgnore
    private TenantSSOConf tenantSSOConf;

    @ManyToOne
    @JsonIgnoreProperties("ssoConfigs")
    private Tenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
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

    public String getDsplName() {
        return dsplName;
    }

    public SSOConfiguration dsplName(String dsplName) {
        this.dsplName = dsplName;
        return this;
    }

    public void setDsplName(String dsplName) {
        this.dsplName = dsplName;
    }

    public String getIdpIssuer() {
        return idpIssuer;
    }

    public SSOConfiguration idpIssuer(String idpIssuer) {
        this.idpIssuer = idpIssuer;
        return this;
    }

    public void setIdpIssuer(String idpIssuer) {
        this.idpIssuer = idpIssuer;
    }

    public String getIdpReqURL() {
        return idpReqURL;
    }

    public SSOConfiguration idpReqURL(String idpReqURL) {
        this.idpReqURL = idpReqURL;
        return this;
    }

    public void setIdpReqURL(String idpReqURL) {
        this.idpReqURL = idpReqURL;
    }

    public String getSpConsumerURL() {
        return spConsumerURL;
    }

    public SSOConfiguration spConsumerURL(String spConsumerURL) {
        this.spConsumerURL = spConsumerURL;
        return this;
    }

    public void setSpConsumerURL(String spConsumerURL) {
        this.spConsumerURL = spConsumerURL;
    }

    public String getSpIssuer() {
        return spIssuer;
    }

    public SSOConfiguration spIssuer(String spIssuer) {
        this.spIssuer = spIssuer;
        return this;
    }

    public void setSpIssuer(String spIssuer) {
        this.spIssuer = spIssuer;
    }

    public Integer getAttribIndex() {
        return attribIndex;
    }

    public SSOConfiguration attribIndex(Integer attribIndex) {
        this.attribIndex = attribIndex;
        return this;
    }

    public void setAttribIndex(Integer attribIndex) {
        this.attribIndex = attribIndex;
    }

    public Boolean isValidateRespSignature() {
        return validateRespSignature;
    }

    public SSOConfiguration validateRespSignature(Boolean validateRespSignature) {
        this.validateRespSignature = validateRespSignature;
        return this;
    }

    public void setValidateRespSignature(Boolean validateRespSignature) {
        this.validateRespSignature = validateRespSignature;
    }

    public Boolean isValidateIdpIssuer() {
        return validateIdpIssuer;
    }

    public SSOConfiguration validateIdpIssuer(Boolean validateIdpIssuer) {
        this.validateIdpIssuer = validateIdpIssuer;
        return this;
    }

    public void setValidateIdpIssuer(Boolean validateIdpIssuer) {
        this.validateIdpIssuer = validateIdpIssuer;
    }

    public Boolean isSignRequests() {
        return signRequests;
    }

    public SSOConfiguration signRequests(Boolean signRequests) {
        this.signRequests = signRequests;
        return this;
    }

    public void setSignRequests(Boolean signRequests) {
        this.signRequests = signRequests;
    }

    public Boolean isAllowLogout() {
        return allowLogout;
    }

    public SSOConfiguration allowLogout(Boolean allowLogout) {
        this.allowLogout = allowLogout;
        return this;
    }

    public void setAllowLogout(Boolean allowLogout) {
        this.allowLogout = allowLogout;
    }

    public String getNonSamlLogoutURL() {
        return nonSamlLogoutURL;
    }

    public SSOConfiguration nonSamlLogoutURL(String nonSamlLogoutURL) {
        this.nonSamlLogoutURL = nonSamlLogoutURL;
        return this;
    }

    public void setNonSamlLogoutURL(String nonSamlLogoutURL) {
        this.nonSamlLogoutURL = nonSamlLogoutURL;
    }

    public Boolean isRedirectToApplication() {
        return redirectToApplication;
    }

    public SSOConfiguration redirectToApplication(Boolean redirectToApplication) {
        this.redirectToApplication = redirectToApplication;
        return this;
    }

    public void setRedirectToApplication(Boolean redirectToApplication) {
        this.redirectToApplication = redirectToApplication;
    }

    public String getAppRedirectURL() {
        return appRedirectURL;
    }

    public SSOConfiguration appRedirectURL(String appRedirectURL) {
        this.appRedirectURL = appRedirectURL;
        return this;
    }

    public void setAppRedirectURL(String appRedirectURL) {
        this.appRedirectURL = appRedirectURL;
    }

    public String getCertAlias() {
        return certAlias;
    }

    public SSOConfiguration certAlias(String certAlias) {
        this.certAlias = certAlias;
        return this;
    }

    public void setCertAlias(String certAlias) {
        this.certAlias = certAlias;
    }

    public String getCertPassword() {
        return certPassword;
    }

    public SSOConfiguration certPassword(String certPassword) {
        this.certPassword = certPassword;
        return this;
    }

    public void setCertPassword(String certPassword) {
        this.certPassword = certPassword;
    }

    public String getCertText() {
        return certText;
    }

    public SSOConfiguration certText(String certText) {
        this.certText = certText;
        return this;
    }

    public void setCertText(String certText) {
        this.certText = certText;
    }

    public Integer getExpireRequestSecs() {
        return expireRequestSecs;
    }

    public SSOConfiguration expireRequestSecs(Integer expireRequestSecs) {
        this.expireRequestSecs = expireRequestSecs;
        return this;
    }

    public void setExpireRequestSecs(Integer expireRequestSecs) {
        this.expireRequestSecs = expireRequestSecs;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public SSOConfiguration enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public TenantSSOConf getTenantSSOConf() {
        return tenantSSOConf;
    }

    public SSOConfiguration tenantSSOConf(TenantSSOConf tenantSSOConf) {
        this.tenantSSOConf = tenantSSOConf;
        return this;
    }

    public void setTenantSSOConf(TenantSSOConf tenantSSOConf) {
        this.tenantSSOConf = tenantSSOConf;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public SSOConfiguration tenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.dsplName);
        hash = 59 * hash + Objects.hashCode(this.idpIssuer);
        hash = 59 * hash + Objects.hashCode(this.idpReqURL);
        hash = 59 * hash + Objects.hashCode(this.spConsumerURL);
        hash = 59 * hash + Objects.hashCode(this.spIssuer);
        hash = 59 * hash + Objects.hashCode(this.attribIndex);
        hash = 59 * hash + Objects.hashCode(this.validateRespSignature);
        hash = 59 * hash + Objects.hashCode(this.validateIdpIssuer);
        hash = 59 * hash + Objects.hashCode(this.signRequests);
        hash = 59 * hash + Objects.hashCode(this.allowLogout);
        hash = 59 * hash + Objects.hashCode(this.nonSamlLogoutURL);
        hash = 59 * hash + Objects.hashCode(this.redirectToApplication);
        hash = 59 * hash + Objects.hashCode(this.appRedirectURL);
        hash = 59 * hash + Objects.hashCode(this.certAlias);
        hash = 59 * hash + Objects.hashCode(this.certPassword);
        hash = 59 * hash + Objects.hashCode(this.certText);
        hash = 59 * hash + Objects.hashCode(this.expireRequestSecs);
        hash = 59 * hash + Objects.hashCode(this.enabled);
        hash = 59 * hash + (this.linked ? 1 : 0);
        hash = 59 * hash + Objects.hashCode(this.tenantSSOConf);
        hash = 59 * hash + Objects.hashCode(this.tenant);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SSOConfiguration other = (SSOConfiguration) obj;
        if (this.linked != other.linked) {
            return false;
        }
        if (!Objects.equals(this.dsplName, other.dsplName)) {
            return false;
        }
        if (!Objects.equals(this.idpIssuer, other.idpIssuer)) {
            return false;
        }
        if (!Objects.equals(this.idpReqURL, other.idpReqURL)) {
            return false;
        }
        if (!Objects.equals(this.spConsumerURL, other.spConsumerURL)) {
            return false;
        }
        if (!Objects.equals(this.spIssuer, other.spIssuer)) {
            return false;
        }
        if (!Objects.equals(this.nonSamlLogoutURL, other.nonSamlLogoutURL)) {
            return false;
        }
        if (!Objects.equals(this.appRedirectURL, other.appRedirectURL)) {
            return false;
        }
        if (!Objects.equals(this.certAlias, other.certAlias)) {
            return false;
        }
        if (!Objects.equals(this.certPassword, other.certPassword)) {
            return false;
        }
        if (!Objects.equals(this.certText, other.certText)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.attribIndex, other.attribIndex)) {
            return false;
        }
        if (!Objects.equals(this.validateRespSignature, other.validateRespSignature)) {
            return false;
        }
        if (!Objects.equals(this.validateIdpIssuer, other.validateIdpIssuer)) {
            return false;
        }
        if (!Objects.equals(this.signRequests, other.signRequests)) {
            return false;
        }
        if (!Objects.equals(this.allowLogout, other.allowLogout)) {
            return false;
        }
        if (!Objects.equals(this.redirectToApplication, other.redirectToApplication)) {
            return false;
        }
        if (!Objects.equals(this.expireRequestSecs, other.expireRequestSecs)) {
            return false;
        }
        if (!Objects.equals(this.enabled, other.enabled)) {
            return false;
        }
        if (!Objects.equals(this.tenantSSOConf, other.tenantSSOConf)) {
            return false;
        }
        if (!Objects.equals(this.tenant, other.tenant)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SSOConfiguration{" + "id=" + id + ", dsplName=" + dsplName + ", idpIssuer=" + idpIssuer + ", idpReqURL=" + idpReqURL + ", spConsumerURL=" + spConsumerURL + ", spIssuer=" + spIssuer + ", attribIndex=" + attribIndex + ", validateRespSignature=" + validateRespSignature + ", validateIdpIssuer=" + validateIdpIssuer + ", signRequests=" + signRequests + ", allowLogout=" + allowLogout + ", nonSamlLogoutURL=" + nonSamlLogoutURL + ", redirectToApplication=" + redirectToApplication + ", appRedirectURL=" + appRedirectURL + ", certAlias=" + certAlias + ", certPassword=" + certPassword + ", certText=" + certText + ", expireRequestSecs=" + expireRequestSecs + ", enabled=" + enabled + ", linked=" + linked + ", tenantSSOConf=" + tenantSSOConf + ", tenant=" + tenant + '}';
    }

}
