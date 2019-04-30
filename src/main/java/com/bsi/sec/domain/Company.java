package com.bsi.sec.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "dataset", nullable = false)
    private String dataset;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "saml_cid", nullable = false, unique = true)
    private String samlCid;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @NotNull
    @Column(name = "imported", nullable = false)
    private Boolean imported;

    @CreatedDate
    @Column(name = "imported_date", updatable = false)
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Company company = (Company) o;
        if (company.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), company.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Company{" + "id=" + id + ", dataset=" + dataset + ", name=" + name + ", samlCid=" + samlCid + ", enabled=" + enabled + ", imported=" + imported + ", importedDate=" + importedDate + ", importedBy=" + importedBy + ", tenant=" + tenant + '}';
    }

}
