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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "BTOCOMP")
@XmlRootElement
public class Btocomp implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "FEIN")
    private String fein;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "LEGALNAME")
    private String legalname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 57)
    @Column(name = "STREET1")
    private String street1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 57)
    @Column(name = "STREET2")
    private String street2;
    @Size(max = 57)
    @Column(name = "CITY")
    private String city;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 23)
    @Column(name = "STATE")
    private String state;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 15)
    @Column(name = "POSTCODE")
    private String postcode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "COUNTRY")
    private String country;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "CONTACT")
    private String contact;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "CPHONE")
    private String cphone;
    @Size(max = 4)
    @Column(name = "CPHONEE")
    private String cphonee;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 254)
    @Column(name = "CEMAIL")
    private String cemail;
    @Lob()
    @Column(name = "BRANDING")
    private byte[] branding;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 254)
    @Column(name = "SKIN")
    private String skin;
    @Size(max = 256)
    @Column(name = "SAMLCID")
    private String samlcid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ENABLED")
    private boolean enabled;
    @Basic(optional = false)
    @NotNull()
    @Column(name = "CREATEDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdat;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "btocomp")
    private Collection<Btoconfig> btoconfigCollection;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BtocompPK btocompPK;
    @Column(name = "UPDATEDAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedat;
    @JoinColumn(name = "DATASETID", referencedColumnName = "DATASETID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Btodset btodset;

    public Btocomp() {
    }

    public Btocomp(BtocompPK btocompPK) {
        this.btocompPK = btocompPK;
    }

    public Btocomp(BtocompPK btocompPK, String fein, String legalname, String street1, String street2, String state, String postcode, String country, String contact, String cphone, String cemail, String skin, boolean enabled, Date createdat) {
        this.btocompPK = btocompPK;
        this.fein = fein;
        this.legalname = legalname;
        this.street1 = street1;
        this.street2 = street2;
        this.state = state;
        this.postcode = postcode;
        this.country = country;
        this.contact = contact;
        this.cphone = cphone;
        this.cemail = cemail;
        this.skin = skin;
        this.enabled = enabled;
        this.createdat = createdat;
    }

    public Btocomp(int datasetid, int compid) {
        this.btocompPK = new BtocompPK(datasetid, compid);
    }

    public BtocompPK getBtocompPK() {
        return btocompPK;
    }

    public void setBtocompPK(BtocompPK btocompPK) {
        this.btocompPK = btocompPK;
    }
    public Date getUpdatedat() {
        return updatedat;
    }
    public void setUpdatedat(Date updatedat) {
        this.updatedat = updatedat;
    }
    public Btodset getBtodset() {
        return btodset;
    }
    public void setBtodset(Btodset btodset) {
        this.btodset = btodset;
    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (btocompPK != null ? btocompPK.hashCode() : 0);
        return hash;
    }
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Btocomp)) {
            return false;
        }
        Btocomp other = (Btocomp) object;
        if ((this.btocompPK == null && other.btocompPK != null) || (this.btocompPK != null && !this.btocompPK.equals(other.btocompPK))) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "com.bsi.tpf.model.Btocomp[ btocompPK=" + btocompPK + " ]";
    }

    public String getFein() {
        return fein;
    }

    public void setFein(String fein) {
        this.fein = fein;
    }

    public String getLegalname() {
        return legalname;
    }

    public void setLegalname(String legalname) {
        this.legalname = legalname;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCphone() {
        return cphone;
    }

    public void setCphone(String cphone) {
        this.cphone = cphone;
    }

    public String getCphonee() {
        return cphonee;
    }

    public void setCphonee(String cphonee) {
        this.cphonee = cphonee;
    }

    public String getCemail() {
        return cemail;
    }

    public void setCemail(String cemail) {
        this.cemail = cemail;
    }

    public byte[] getBranding() {
        return branding;
    }

    public void setBranding(byte[] branding) {
        this.branding = branding;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getSamlcid() {
        return samlcid;
    }

    public void setSamlcid(String samlcid) {
        this.samlcid = samlcid;
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

    @XmlTransient
    public Collection<Btoconfig> getBtoconfigCollection() {
        return btoconfigCollection;
    }

    public void setBtoconfigCollection(Collection<Btoconfig> btoconfigCollection) {
        this.btoconfigCollection = btoconfigCollection;
    }
    
}
