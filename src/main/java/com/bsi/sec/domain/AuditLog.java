package com.bsi.sec.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import org.hibernate.envers.Audited;

/**
 * A AuditLog.
 */
@Entity
@Audited
@Table(name = "audit_log")
public class AuditLog extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "server_host", nullable = false)
    private String serverHost;

    @NotNull
    @Column(name = "jhi_user", nullable = false)
    private String user;

    @Column(name = "client_host")
    private String clientHost;

    @NotNull
    @Column(name = "operation", nullable = false)
    private String operation;

    @NotNull
    @Column(name = "jhi_account", nullable = false)
    private String account;

    @NotNull
    @Column(name = "product", nullable = false)
    private String product;

    @NotNull
    @Column(name = "dataset", nullable = false)
    private String dataset;

    @NotNull
    @Column(name = "area", nullable = false)
    private String area;

    @Column(name = "message")
    private String message;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServerHost() {
        return serverHost;
    }

    public AuditLog serverHost(String serverHost) {
        this.serverHost = serverHost;
        return this;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getUser() {
        return user;
    }

    public AuditLog user(String user) {
        this.user = user;
        return this;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getClientHost() {
        return clientHost;
    }

    public AuditLog clientHost(String clientHost) {
        this.clientHost = clientHost;
        return this;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public String getOperation() {
        return operation;
    }

    public AuditLog operation(String operation) {
        this.operation = operation;
        return this;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getAccount() {
        return account;
    }

    public AuditLog account(String account) {
        this.account = account;
        return this;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getProduct() {
        return product;
    }

    public AuditLog product(String product) {
        this.product = product;
        return this;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDataset() {
        return dataset;
    }

    public AuditLog dataset(String dataset) {
        this.dataset = dataset;
        return this;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getArea() {
        return area;
    }

    public AuditLog area(String area) {
        this.area = area;
        return this;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getMessage() {
        return message;
    }

    public AuditLog message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
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
        AuditLog auditLog = (AuditLog) o;
        if (auditLog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), auditLog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AuditLog{"
                + "id=" + getId()
                + ", serverHost='" + getServerHost() + "'"
                + ", user='" + getUser() + "'"
                + ", clientHost='" + getClientHost() + "'"
                + ", operation='" + getOperation() + "'"
                + ", account='" + getAccount() + "'"
                + ", product='" + getProduct() + "'"
                + ", dataset='" + getDataset() + "'"
                + ", area='" + getArea() + "'"
                + ", message='" + getMessage() + "'"
                + "}";
    }
}
