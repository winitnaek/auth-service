/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dto;

import javax.validation.constraints.Min;

/**
 *
 * @author igorV
 */
public final class ProductDTO {

    @Min(1L)
    private long id;

    private String acctName;

    private String prodName;

    public ProductDTO() {

    }

    public ProductDTO(Long id, String acctName, String prodName) {
        this.id = id;
        this.acctName = acctName;
        this.prodName = prodName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    @Override
    public String toString() {
        return "ProductDTO{" + "id=" + id + ", acctName=" + acctName + ", prodName=" + prodName + '}';
    }

}
