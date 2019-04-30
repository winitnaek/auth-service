package com.bsi.sec.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
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
    private Boolean enabled;

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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SSOConfiguration sSOConfiguration = (SSOConfiguration) o;
        if (sSOConfiguration.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sSOConfiguration.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SSOConfiguration{"
                + "id=" + getId()
                + ", dsplName='" + getDsplName() + "'"
                + ", idpIssuer='" + getIdpIssuer() + "'"
                + ", idpReqURL='" + getIdpReqURL() + "'"
                + ", spConsumerURL='" + getSpConsumerURL() + "'"
                + ", spIssuer='" + getSpIssuer() + "'"
                + ", attribIndex=" + getAttribIndex()
                + ", validateRespSignature='" + isValidateRespSignature() + "'"
                + ", validateIdpIssuer='" + isValidateIdpIssuer() + "'"
                + ", signRequests='" + isSignRequests() + "'"
                + ", allowLogout='" + isAllowLogout() + "'"
                + ", nonSamlLogoutURL='" + getNonSamlLogoutURL() + "'"
                + ", redirectToApplication='" + isRedirectToApplication() + "'"
                + ", appRedirectURL='" + getAppRedirectURL() + "'"
                + ", certAlias='" + getCertAlias() + "'"
                + ", certPassword='" + getCertPassword() + "'"
                + ", certText='" + getCertText() + "'"
                + ", expireRequestSecs=" + getExpireRequestSecs()
                + ", enabled='" + isEnabled() + "'"
                + "}";
    }
}
