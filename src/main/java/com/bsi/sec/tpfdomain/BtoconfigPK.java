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
import javax.validation.constraints.Size;

/**
 *
 * @author SudhirP
 */
@Embeddable
public class BtoconfigPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "DATASETID")
    private int datasetid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "COMPID")
    private int compid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ITEMNAME")
    private String itemname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CATGRY")
    private short catgry;
    @Basic(optional = false)
    @NotNull
    @Column(name = "SUBCAT")
    private short subcat;

    public BtoconfigPK() {
    }

    public BtoconfigPK(int datasetid, int compid, String itemname, short catgry, short subcat) {
        this.datasetid = datasetid;
        this.compid = compid;
        this.itemname = itemname;
        this.catgry = catgry;
        this.subcat = subcat;
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

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public short getCatgry() {
        return catgry;
    }

    public void setCatgry(short catgry) {
        this.catgry = catgry;
    }

    public short getSubcat() {
        return subcat;
    }

    public void setSubcat(short subcat) {
        this.subcat = subcat;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) datasetid;
        hash += (int) compid;
        hash += (itemname != null ? itemname.hashCode() : 0);
        hash += (int) catgry;
        hash += (int) subcat;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BtoconfigPK)) {
            return false;
        }
        BtoconfigPK other = (BtoconfigPK) object;
        if (this.datasetid != other.datasetid) {
            return false;
        }
        if (this.compid != other.compid) {
            return false;
        }
        if ((this.itemname == null && other.itemname != null) || (this.itemname != null && !this.itemname.equals(other.itemname))) {
            return false;
        }
        if (this.catgry != other.catgry) {
            return false;
        }
        if (this.subcat != other.subcat) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bsi.tpf.model.BtoconfigPK[ datasetid=" + datasetid + ", compid=" + compid + ", itemname=" + itemname + ", catgry=" + catgry + ", subcat=" + subcat + " ]";
    }
    
}
