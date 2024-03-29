/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Min;

/**
 * TODO: Add JSR-303 validation, comments.
 *
 * @author igorV
 */
public final class TenantDTO {

    @Min(1L)
    private Long id;

    @NotNull
    private String acctId;

    @NotNull
    private String acctName;

    @NotNull
    private String prodId;

    @NotNull
    private String prodName;

    @NotNull
    private String dataset;

    @NotNull
    private Long ssoConfId;

    private String ssoConfDsplName;

    private boolean enabled;

    private boolean imported;

    public TenantDTO() {
    }

    public TenantDTO(Long id, String acctName, String prodName, String dataset) {
        this.id = id;
        this.acctName = acctName;
        this.prodName = prodName;
        this.dataset = dataset;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getAcctName() {
        return acctName;
    }

    public void setAcctName(String acctName) {
        this.acctName = acctName;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public Long getSsoConfId() {
        return ssoConfId;
    }

    public void setSsoConfId(Long ssoConfId) {
        this.ssoConfId = ssoConfId;
    }

    public String getSsoConfDsplName() {
        return ssoConfDsplName;
    }

    public void setSsoConfDsplName(String ssoConfDsplName) {
        this.ssoConfDsplName = ssoConfDsplName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }

    @Override
    public String toString() {
        return "TenantDTO{" + "id=" + id + ", acctId=" + acctId + ", acctName=" + acctName + ", prodId=" + prodId + ", prodName=" + prodName + ", dataset=" + dataset + ", ssoConfId=" + ssoConfId + ", ssoConfDsplName=" + ssoConfDsplName + ", enabled=" + enabled + ", imported=" + imported + '}';
    }

}
