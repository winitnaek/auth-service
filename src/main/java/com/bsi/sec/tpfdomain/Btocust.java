/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.tpfdomain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author SudhirP
 */
@Entity
@Table(name = "BTOCUST")
@XmlRootElement
public class Btocust implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CUSTNAME")
    private String custname;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ENABLED")
    private boolean enabled;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CREATEDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdat;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CUSTID")
    private Integer custid;
    @Column(name = "DEFAULTDSET")
    private Integer defaultdset;
    @Column(name = "UPDATEDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedat;
    @OneToMany(mappedBy = "custid")
    private Collection<Btodset> btodsetCollection;

    public Btocust() {
    }

    public Btocust(Integer custid) {
        this.custid = custid;
    }

    public Btocust(Integer custid, String custname, boolean enabled, Date createdat) {
        this.custid = custid;
        this.custname = custname;
        this.enabled = enabled;
        this.createdat = createdat;
    }

    public Integer getCustid() {
        return custid;
    }

    public void setCustid(Integer custid) {
        this.custid = custid;
    }

    public Integer getDefaultdset() {
        return defaultdset;
    }

    public void setDefaultdset(Integer defaultdset) {
        this.defaultdset = defaultdset;
    }


    public Date getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Date updatedat) {
        this.updatedat = updatedat;
    }

    @XmlTransient
    public Collection<Btodset> getBtodsetCollection() {
        return btodsetCollection;
    }

    public void setBtodsetCollection(Collection<Btodset> btodsetCollection) {
        this.btodsetCollection = btodsetCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (custid != null ? custid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Btocust)) {
            return false;
        }
        Btocust other = (Btocust) object;
        if ((this.custid == null && other.custid != null) || (this.custid != null && !this.custid.equals(other.custid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bsi.tpf.model.Btocust[ custid=" + custid + " ]";
    }

    public String getCustname() {
        return custname;
    }

    public void setCustname(String custname) {
        this.custname = custname;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Date getCreatedat() {
        return createdat;
    }

    public void setCreatedat(Date createdat) {
        this.createdat = createdat;
    }
}