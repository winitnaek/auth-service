package com.bsi.sec.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A Company.
 */
@Entity
@Table(name = "company")
@Audited
@EntityListeners(AuditingEntityListener.class)
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @QuerySqlField(index = true)
    private Long id;

    @NotNull
    @Column(name = "dataset", nullable = false)
    @QuerySqlField(index = true)
    private String dataset;

    @NotNull
    @Column(name = "name", nullable = false)
    @QuerySqlField
    private String name;

    @NotNull
    @Column(name = "saml_cid", nullable = false, unique = true)
    @QuerySqlField(index = true)
    private String samlCid;

    @NotNull
    @Column(name = "enabled", nullable = false)
    @QuerySqlField
    private Boolean enabled;

    @NotNull
    @Column(name = "imported", nullable = false)
    @QuerySqlField
    private Boolean imported;

    @CreatedDate
    @Column(name = "imported_date", updatable = false)
    @QuerySqlField
    @JsonIgnore
    private Instant importedDate = Instant.now();

    @Column(name = "imported_by")
    @CreatedBy
    private String importedBy;

    @ManyToOne
    @JsonIgnoreProperties("companies")
    private Tenant tenant;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataset() {
        return dataset;
    }

    public Company dataset(String dataset) {
        this.dataset = dataset;
        return this;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getName() {
        return name;
    }

    public Company name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSamlCid() {
        return samlCid;
    }

    public Company samlCid(String samlCid) {
        this.samlCid = samlCid;
        return this;
    }

    public void setSamlCid(String samlCid) {
        this.samlCid = samlCid;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Company enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean isImported() {
        return imported;
    }

    public Company imported(Boolean imported) {
        this.imported = imported;
        return this;
    }

    public void setImported(Boolean imported) {
        this.imported = imported;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public Company tenant(Tenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    public Instant getImportedDate() {
        return importedDate;
    }

    public void setImportedDate(Instant importedDate) {
        this.importedDate = importedDate;
    }

    public String getImportedBy() {
        return importedBy;
    }

    public void setImportedBy(String importedBy) {
        this.importedBy = importedBy;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.id);
        hash = 11 * hash + Objects.hashCode(this.dataset);
        hash = 11 * hash + Objects.hashCode(this.name);
        hash = 11 * hash + Objects.hashCode(this.samlCid);
        hash = 11 * hash + Objects.hashCode(this.enabled);
        hash = 11 * hash + Objects.hashCode(this.imported);
        hash = 11 * hash + Objects.hashCode(this.importedDate);
        hash = 11 * hash + Objects.hashCode(this.importedBy);
        hash = 11 * hash + Objects.hashCode(this.tenant);
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
        final Company other = (Company) obj;
        if (!Objects.equals(this.dataset, other.dataset)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.samlCid, other.samlCid)) {
            return false;
        }
        if (!Objects.equals(this.importedBy, other.importedBy)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.enabled, other.enabled)) {
            return false;
        }
        if (!Objects.equals(this.imported, other.imported)) {
            return false;
        }
        if (!Objects.equals(this.importedDate, other.importedDate)) {
            return false;
        }
        if (!Objects.equals(this.tenant, other.tenant)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Company{" + "id=" + id + ", dataset=" + dataset + ", name=" + name + ", samlCid=" + samlCid + ", enabled=" + enabled + ", imported=" + imported + ", importedDate=" + importedDate + ", importedBy=" + importedBy + ", tenant=" + tenant + '}';
    }

}
