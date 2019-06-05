package com.bsi.sec.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.hibernate.annotations.NaturalId;
import org.hibernate.envers.Audited;

/**
 * A Tenant.
 */
@Entity
@Audited
@Table(name = "tenant")
public class Tenant extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @QuerySqlField(index = true)
    private Long id;

    @NaturalId
    @NotNull
    @Column(name = "acct_id", nullable = false)
    private String acctId;

    @NotNull
    @Column(name = "acct_name", nullable = false)
    @QuerySqlField(index = true)
    private String acctName;

    @NotNull
    @Column(name = "prod_id", nullable = true)
    @QuerySqlField(index = true)
    private String prodId;

    @NotNull
    @Column(name = "prod_name", nullable = false)
    @QuerySqlField(index = true)
    private String prodName;

    @NotNull
    @Column(name = "dataset", nullable = false)
    @QuerySqlField(index = true)
    private String dataset;

    @Column(name = "enabled", nullable = false)
    @QuerySqlField(name = "enabled")
    private boolean enabled;

    @Column(name = "imported", nullable = false)
    @QuerySqlField(name = "imported")
    private boolean imported;

    @Column(name = "conf_id", nullable = true)
    @QuerySqlField(name = "confId")
    private Long confId;

    @Column(name = "conf_id_dspl_name", nullable = true)
    @QuerySqlField(name = "confIdDsplName")
    private String confIdDsplName;

    @OneToMany(mappedBy = "tenant")
    private Set<Company> companies = new HashSet<>();
    @OneToMany(mappedBy = "tenant")
    private Set<SSOConfiguration> ssoConfigs = new HashSet<>();
    @OneToOne(mappedBy = "tenant")
    @JsonIgnore
    private TenantSSOConf tenantSSOConf;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcctId() {
        return acctId;
    }

    public Tenant acctId(String acctId) {
        this.acctId = acctId;
        return this;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getAcctName() {
        return acctName;
    }

    public Tenant acctName(String acctName) {
        this.acctName = acctName;
        return this;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getProdId() {
        return prodId;
    }

    public Tenant prodId(String prodId) {
        this.prodId = prodId;
        return this;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public Tenant prodName(String prodName) {
        this.prodName = prodName;
        return this;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getDataset() {
        return dataset;
    }

    public Tenant dataset(String dataset) {
        this.dataset = dataset;
        return this;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public Long getConfId() {
        return confId;
    }

    public void setConfId(Long confId) {
        this.confId = confId;
    }

    public String getConfIdDsplName() {
        return confIdDsplName;
    }

    public void setConfIdDsplName(String confIdDsplName) {
        this.confIdDsplName = confIdDsplName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Tenant enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isImported() {
        return imported;
    }

    public Tenant imported(boolean imported) {
        this.imported = imported;
        return this;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }

    public Set<Company> getCompanies() {
        return companies;
    }

    public Tenant companies(Set<Company> companies) {
        this.companies = companies;
        return this;
    }

    public Tenant addCompany(Company company) {
        this.companies.add(company);
        company.setTenant(this);
        return this;
    }

    public Tenant removeCompany(Company company) {
        this.companies.remove(company);
        company.setTenant(null);
        return this;
    }

    public void setCompanies(Set<Company> companies) {
        this.companies = companies;
    }

    public Set<SSOConfiguration> getSsoConfigs() {
        return ssoConfigs;
    }

    public Tenant ssoConfigs(Set<SSOConfiguration> sSOConfigurations) {
        this.ssoConfigs = sSOConfigurations;
        return this;
    }

    public Tenant addSsoConfig(SSOConfiguration sSOConfiguration) {
        this.ssoConfigs.add(sSOConfiguration);
        sSOConfiguration.setTenant(this);
        return this;
    }

    public Tenant removeSsoConfig(SSOConfiguration sSOConfiguration) {
        this.ssoConfigs.remove(sSOConfiguration);
        sSOConfiguration.setTenant(null);
        return this;
    }

    public void setSsoConfigs(Set<SSOConfiguration> sSOConfigurations) {
        this.ssoConfigs = sSOConfigurations;
    }

    public TenantSSOConf getTenantSSOConf() {
        return tenantSSOConf;
    }

    public Tenant tenantSSOConf(TenantSSOConf tenantSSOConf) {
        this.tenantSSOConf = tenantSSOConf;
        return this;
    }

    public void setTenantSSOConf(TenantSSOConf tenantSSOConf) {
        this.tenantSSOConf = tenantSSOConf;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
        hash = 73 * hash + Objects.hashCode(this.acctId);
        hash = 73 * hash + Objects.hashCode(this.acctName);
        hash = 73 * hash + Objects.hashCode(this.prodId);
        hash = 73 * hash + Objects.hashCode(this.prodName);
        hash = 73 * hash + Objects.hashCode(this.dataset);
        hash = 73 * hash + (this.enabled ? 1 : 0);
        hash = 73 * hash + (this.imported ? 1 : 0);
        hash = 73 * hash + Objects.hashCode(this.confId);
        hash = 73 * hash + Objects.hashCode(this.confIdDsplName);
        hash = 73 * hash + Objects.hashCode(this.companies);
        hash = 73 * hash + Objects.hashCode(this.ssoConfigs);
        hash = 73 * hash + Objects.hashCode(this.tenantSSOConf);
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
        final Tenant other = (Tenant) obj;
        if (this.enabled != other.enabled) {
            return false;
        }
        if (this.imported != other.imported) {
            return false;
        }
        if (!Objects.equals(this.acctId, other.acctId)) {
            return false;
        }
        if (!Objects.equals(this.acctName, other.acctName)) {
            return false;
        }
        if (!Objects.equals(this.prodId, other.prodId)) {
            return false;
        }
        if (!Objects.equals(this.prodName, other.prodName)) {
            return false;
        }
        if (!Objects.equals(this.dataset, other.dataset)) {
            return false;
        }
        if (!Objects.equals(this.confIdDsplName, other.confIdDsplName)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.confId, other.confId)) {
            return false;
        }
        if (!Objects.equals(this.companies, other.companies)) {
            return false;
        }
        if (!Objects.equals(this.ssoConfigs, other.ssoConfigs)) {
            return false;
        }
        if (!Objects.equals(this.tenantSSOConf, other.tenantSSOConf)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tenant{" + "id=" + id + ", acctId=" + acctId + ", acctName=" + acctName + ", prodId=" + prodId + ", prodName=" + prodName + ", dataset=" + dataset + ", enabled=" + enabled + ", imported=" + imported + ", confId=" + confId + ", confIdDsplName=" + confIdDsplName + ", companies=" + companies + ", ssoConfigs=" + ssoConfigs + ", tenantSSOConf=" + tenantSSOConf + '}';
    }

}
