/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Instant;
/**
 *
 * @author igorV
 */
public final class AuditLogDTO {

    @NotNull
    @Min(1)
    private Long id;
    
    @NotNull
    private Instant createdDate;

    @NotNull
    private String serverHost;

    @NotNull
    private String user;

    private String clientHost;

    @NotNull
    private String operation;

    @NotNull
    private String account;

    @NotNull
    private String product;

    @NotNull
    private String dataset;

    @NotNull
    private String area;

    private String message;

    public AuditLogDTO() {
    }

    /**
     * AuditLogDTO
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
    public AuditLogDTO(Long id, Instant createdDate, String serverHost, String user, String clientHost, String operation, String account, String product, String dataset, String area, String message) {
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

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
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