/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.tpfdomain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author SudhirP
 */
@Embeddable
public class BtocompPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATASETID")
    private int datasetid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COMPID")
    private int compid;

    public BtocompPK() {
    }

    public BtocompPK(int datasetid, int compid) {
        this.datasetid = datasetid;
        this.compid = compid;
    }

    public int getDatasetid() {
        return datasetid;
    }

    public void setDatasetid(int datasetid) {
        this.datasetid = datasetid;
    }

    public int getCompid() {
        return compid;
    }

    public void setCompid(int compid) {
        this.compid = compid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) datasetid;
        hash += (int) compid;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BtocompPK)) {
            return false;
        }
        BtocompPK other = (BtocompPK) object;
        if (this.datasetid != other.datasetid) {
            return false;
        }
        if (this.compid != other.compid) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bsi.tpf.model.BtocompPK[ datasetid=" + datasetid + ", compid=" + compid + " ]";
    }
    
}
