/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author igorV
 */
public final class DatasetProductDTO {

    @Min(1L)
    private Long id;

    @NotNull
    private String acctName;

    @NotNull
    private String prodName;

    @NotNull
    private String dataset;

    public DatasetProductDTO() {
    }

    public DatasetProductDTO(Long id, String acctName, String prodName, String dataset) {
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

    @Override
    public String toString() {
        return "DatasetProductDTO{" + "id=" + id + ", acctName=" + acctName + ", prodName=" + prodName + ", dataset=" + dataset + '}';
    }

}
