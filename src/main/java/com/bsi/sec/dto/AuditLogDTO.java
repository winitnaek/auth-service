/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dto;

import com.bsi.sec.svc.AuditLogger;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

/**
 *
 * @author igorV
 */
public final class AuditLogDTO {

    @Min(1)
    private Long id;

    private LocalDateTime createdDate;

    private String serverHost;

    private String user;

    private String clientHost;

    private AuditLogger.Ops operation;

    private String account;

    private String product;

    private String dataset;

    private AuditLogger.Areas area;

    private String message;

    public AuditLogDTO() {
    }

    /**
     * AuditLogDTO
     *
     * @param id
     * @param createdDate
     * @param serverHost
     * @param user
     * @param clientHost
     * @param operation
     * @param account
     * @param product
     * @param dataset
     * @param area
     * @param message
     */
    public AuditLogDTO(Long id, LocalDateTime createdDate, String serverHost, String user, String clientHost, AuditLogger.Ops operation, String account, String product, String dataset, AuditLogger.Areas area, String message) {
        this.id = id;
        this.createdDate = createdDate;
        this.serverHost = serverHost;
        this.user = user;
        this.clientHost = clientHost;
        this.operation = operation;
        this.account = account;
        this.product = product;
        this.dataset = dataset;
        this.area = area;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getServerHost() {
        return serverHost;
    }

    public void setServerHost(String serverHost) {
        this.serverHost = serverHost;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public AuditLogger.Ops getOperation() {
        return operation;
    }

    public void setOperation(AuditLogger.Ops operation) {
        this.operation = operation;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public AuditLogger.Areas getArea() {
        return area;
    }

    public void setArea(AuditLogger.Areas area) {
        this.area = area;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuditLogDTO{" + "id=" + id + ", createdDate=" + createdDate + ", serverHost=" + serverHost + ", user=" + user + ", clientHost=" + clientHost + ", operation=" + operation + ", account=" + account + ", product=" + product + ", dataset=" + dataset + ", area=" + area + ", message=" + message + '}';
    }
}
