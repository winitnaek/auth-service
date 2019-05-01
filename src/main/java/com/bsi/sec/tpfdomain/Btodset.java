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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "BTODSET")
@XmlRootElement
public class Btodset implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "DESCR")
    private String descr;
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
    @Column(name = "DATASETID")
    private Integer datasetid;
    @Column(name = "UPDATEDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedat;
    @JoinColumn(name = "CUSTID", referencedColumnName = "CUSTID")
    @ManyToOne
    private Btocust custid;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "btodset")
    private Collection<Btocomp> btocompCollection;

    public Btodset() {
    }

    public Btodset(Integer datasetid) {
        this.datasetid = datasetid;
    }

    public Btodset(Integer datasetid, String name, String descr, boolean enabled, Date createdat) {
        this.datasetid = datasetid;
        this.name = name;
        this.descr = descr;
        this.enabled = enabled;
        this.createdat = createdat;
    }

    public Integer getDatasetid() {
        return datasetid;
    }

    public void setDatasetid(Integer datasetid) {
        this.datasetid = datasetid;
    }


    public Date getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(Date updatedat) {
        this.updatedat = updatedat;
    }

    public Btocust getCustid() {
        return custid;
    }

    public void setCustid(Btocust custid) {
        this.custid = custid;
    }

    @XmlTransient
    public Collection<Btocomp> getBtocompCollection() {
        return btocompCollection;
    }

    public void setBtocompCollection(Collection<Btocomp> btocompCollection) {
        this.btocompCollection = btocompCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (datasetid != null ? datasetid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Btodset)) {
            return false;
        }
        Btodset other = (Btodset) object;
        if ((this.datasetid == null && other.datasetid != null) || (this.datasetid != null && !this.datasetid.equals(other.datasetid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.bsi.tpf.model.Btodset[ datasetid=" + datasetid + " ]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
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
