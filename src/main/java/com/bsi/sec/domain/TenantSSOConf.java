package com.bsi.sec.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.hibernate.envers.Audited;

/**
 * A TenantSSOConf.
 */
@Entity
@Audited
@Table(name = "tenant_sso_conf")
public class TenantSSOConf extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @QuerySqlField(index = true)
    private Long id;

    @NotNull
    @Column(name = "acct_name", nullable = false)
    @QuerySqlField(index = true)
    private String acctName;

    @NotNull
    @Column(name = "prod_name", nullable = false)
    @QuerySqlField(index = true)
    private String prodName;

    @NotNull
    @Column(name = "dataset", nullable = false)
    @QuerySqlField(index = true)
    private String dataset;

    @NotNull
    @Column(name = "sso_conf_dspl_name", nullable = false)
    private String ssoConfDsplName;

    @OneToOne
    @JoinColumn(unique = true)
    private Tenant tenant;

    @OneToOne
    @JoinColumn(unique = true)
    private SSOConfiguration ssoConfig;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcctName() {
        return acctName;
    }

    public TenantSSOConf acctName(String acctName) {
        this.acctName = acctName;
        return this;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getProdName() {
        return prodName;
    }

    public TenantSSOConf prodName(String prodName) {
        this.prodName = prodName;
        return this;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getDataset() {
        return dataset;
    }

    public TenantSSOConf dataset(String dataset) {
        this.dataset = dataset;
        return this;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getSsoConfDsplName() {
        return ssoConfDsplName;
    }

    public TenantSSOConf ssoConfDsplName(String ssoConfDsplName) {
        this.ssoConfDsplName = ssoConfDsplName;
        return this;
    }

    public void setSsoConfDsplName(String ssoConfDsplName) {
        this.ssoConfDsplName = ssoConfDsplName;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public TenantSSOConf tenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public SSOConfiguration getSsoConfig() {
        return ssoConfig;
    }

    public TenantSSOConf ssoConfig(SSOConfiguration sSOConfiguration) {
        this.ssoConfig = sSOConfiguration;
        return this;
    }

    public void setSsoConfig(SSOConfiguration sSOConfiguration) {
        this.ssoConfig = sSOConfiguration;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.acctName);
        hash = 59 * hash + Objects.hashCode(this.prodName);
        hash = 59 * hash + Objects.hashCode(this.dataset);
        hash = 59 * hash + Objects.hashCode(this.ssoConfDsplName);
        hash = 59 * hash + Objects.hashCode(this.tenant);
        hash = 59 * hash + Objects.hashCode(this.ssoConfig);
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
        final TenantSSOConf other = (TenantSSOConf) obj;
        if (!Objects.equals(this.acctName, other.acctName)) {
            return false;
        }
        if (!Objects.equals(this.prodName, other.prodName)) {
            return false;
        }
        if (!Objects.equals(this.dataset, other.dataset)) {
            return false;
        }
        if (!Objects.equals(this.ssoConfDsplName, other.ssoConfDsplName)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.tenant, other.tenant)) {
            return false;
        }
        if (!Objects.equals(this.ssoConfig, other.ssoConfig)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TenantSSOConf{"
                + "id=" + getId()
                + ", acctName='" + getAcctName() + "'"
                + ", prodName='" + getProdName() + "'"
                + ", dataset='" + getDataset() + "'"
                + ", ssoConfDsplName='" + getSsoConfDsplName() + "'"
                + "}";
    }
}
