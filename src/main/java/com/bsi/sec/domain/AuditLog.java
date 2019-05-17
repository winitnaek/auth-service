package com.bsi.sec.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import org.apache.ignite.cache.query.annotations.QuerySqlField;
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
    @QuerySqlField(index = true)
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
    @Column(name = "account_name", nullable = false)
    private String accountName;

    @NotNull
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull
    @Column(name = "dataset_name", nullable = false)
    private String datasetName;

    @NotNull
    @Column(name = "area", nullable = false)
    private String area;

    @Column(name = "message")
    @QuerySqlField
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

    public String getAccountName() {
        return accountName;
    }

    public AuditLog account(String account) {
        this.accountName = account;
        return this;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getProductName() {
        return productName;
    }

    public AuditLog product(String product) {
        this.productName = product;
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public AuditLog dataset(String dataset) {
        this.datasetName = dataset;
        return this;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.id);
        hash = 61 * hash + Objects.hashCode(this.serverHost);
        hash = 61 * hash + Objects.hashCode(this.user);
        hash = 61 * hash + Objects.hashCode(this.clientHost);
        hash = 61 * hash + Objects.hashCode(this.operation);
        hash = 61 * hash + Objects.hashCode(this.accountName);
        hash = 61 * hash + Objects.hashCode(this.productName);
        hash = 61 * hash + Objects.hashCode(this.datasetName);
        hash = 61 * hash + Objects.hashCode(this.area);
        hash = 61 * hash + Objects.hashCode(this.message);
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
        final AuditLog other = (AuditLog) obj;
        if (!Objects.equals(this.serverHost, other.serverHost)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.clientHost, other.clientHost)) {
            return false;
        }
        if (!Objects.equals(this.operation, other.operation)) {
            return false;
        }
        if (!Objects.equals(this.accountName, other.accountName)) {
            return false;
        }
        if (!Objects.equals(this.productName, other.productName)) {
            return false;
        }
        if (!Objects.equals(this.datasetName, other.datasetName)) {
            return false;
        }
        if (!Objects.equals(this.area, other.area)) {
            return false;
        }
        if (!Objects.equals(this.message, other.message)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AuditLog{" + "id=" + id + ", serverHost=" + serverHost + ", user=" + user + ", clientHost=" + clientHost + ", operation=" + operation + ", accountName=" + accountName + ", productName=" + productName + ", datasetName=" + datasetName + ", area=" + area + ", message=" + message + '}';
    }

}
