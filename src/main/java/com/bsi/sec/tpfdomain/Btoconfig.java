/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.tpfdomain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SudhirP
 */
@Entity
@Table(name = "BTOCONFIG")
@XmlRootElement
public class Btoconfig implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BtoconfigPK btoconfigPK;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 254)
    @Column(name = "VALUE1")
    private String value1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 254)
    @Column(name = "VALUE2")
    private String value2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATEDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdat;
    @Column(name = "UPDATEDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedat;
    @JoinColumns({
        @JoinColumn(name = "DATASETID", referencedColumnName = "DATASETID", insertable = false, updatable = false)
        , @JoinColumn(name = "COMPID", referencedColumnName = "COMPID", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Btocomp btocomp;

    public Btoconfig() {
    }

    public Btoconfig(BtoconfigPK btoconfigPK) {
        this.btoconfigPK = btoconfigPK;
    }

    public Btoconfig(BtoconfigPK btoconfigPK, String value1, String value2, Date createdat) {
        this.btoconfigPK = btoconfigPK;
        this.value1 = value1;
        this.value2 = value2;
        this.createdat = createdat;
    }

    public Btoconfig(int datasetid, int compid, String itemname, short catgry, short subcat) {
        this.btoconfigPK = new BtoconfigPK(datasetid, compid, itemname, catgry, subcat);
    }

    public BtoconfigPK getBtoconfigPK() {
        return btoconfigPK;
    }

    public void setBtoconfigPK(BtoconfigPK btoconfigPK) {
        this.btoconfigPK = btoconfigPK;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }

    public Date getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Date updatedat) {
        this.updatedat = updatedat;
    }

    public Btocomp getBtocomp() {
        return btocomp;
    }

    public void setBtocomp(Btocomp btocomp) {
        this.btocomp = btocomp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (btoconfigPK != null ? btoconfigPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Btoconfig)) {
            return false;
        }
        Btoconfig other = (Btoconfig) object;
        if ((this.btoconfigPK == null && other.btoconfigPK != null) || (this.btoconfigPK != null && !this.btoconfigPK.equals(other.btoconfigPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bsi.tpf.model.Btoconfig[ btoconfigPK=" + btoconfigPK + " ]";
    }
    
}
