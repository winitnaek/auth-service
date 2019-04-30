package com.bsi.sec.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @QuerySqlField(index = true)
    private Long id;

    @NotNull
    @Column(name = "acct_id", nullable = false)
    private String acctId;

    @NotNull
    @Column(name = "acct_name", nullable = false)
    @QuerySqlField(index = true)
    private String acctName;

    @NotNull
    @Column(name = "prod_id", nullable = false)
    private String prodId;

    @NotNull
    @Column(name = "prod_name", nullable = false)
    @QuerySqlField(index = true)
    private String prodName;

    @NotNull
    @Column(name = "dataset", nullable = false)
    @QuerySqlField(index = true)
    private String dataset;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @NotNull
    @Column(name = "imported", nullable = false)
    private Boolean imported;

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

    public Boolean isEnabled() {
        return enabled;
    }

    public Tenant enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean isImported() {
        return imported;
    }

    public Tenant imported(Boolean imported) {
        this.imported = imported;
        return this;
    }

    public void setImported(Boolean imported) {
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
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tenant tenant = (Tenant) o;
        if (tenant.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tenant.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Tenant{"
                + "id=" + getId()
                + ", acctId='" + getAcctId() + "'"
                + ", acctName='" + getAcctName() + "'"
                + ", prodId='" + getProdId() + "'"
                + ", prodName='" + getProdName() + "'"
                + ", dataset='" + getDataset() + "'"
                + ", enabled='" + isEnabled() + "'"
                + ", imported='" + isImported() + "'"
                + "}";
    }
}
