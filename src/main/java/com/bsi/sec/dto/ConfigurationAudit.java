package com.bsi.sec.dto;

import java.io.Serializable;
import java.sql.Timestamp;

public class ConfigurationAudit implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5317118436073103243L;
	private int dsId;
	private int compId;
	private String fein;
	private String legalName;
	private String value;
	private String configItem;
	private String compValue;
	private String compValueDesc;
	private String dsValue;
	private String dsValueDesc;
	private String configItemDesc;
	private Timestamp lastUpdateAt;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLegalName() {
		return legalName;
	}

	public void setLegalName(String legalName) {
		this.legalName = legalName;
	}

	public String getCompValueDesc() {
		return compValueDesc;
	}

	public void setCompValueDesc(String compValueDesc) {
		this.compValueDesc = compValueDesc;
	}

	public String getDsValueDesc() {
		return dsValueDesc;
	}

	public void setDsValueDesc(String dsValueDesc) {
		this.dsValueDesc = dsValueDesc;
	}

	public Timestamp getLastUpdateAt() {
		return lastUpdateAt;
	}

	public void setLastUpdateAt(Timestamp lastUpdateAt) {
		this.lastUpdateAt = lastUpdateAt;
	}

	public String getConfigItemDesc() {
		return configItemDesc;
	}

	public void setConfigItemDesc(String configItemDesc) {
		this.configItemDesc = configItemDesc;
	}

	public String getFein() {
		return fein;
	}

	public void setFein(String fein) {
		this.fein = fein;
	}

	public String getConfigItem() {
		return configItem;
	}

	public void setConfigItem(String configItem) {
		this.configItem = configItem;
	}

	public String getCompValue() {
		return compValue;
	}

	public void setCompValue(String compValue) {
		this.compValue = compValue;
	}

	public String getDsValue() {
		return dsValue;
	}

	public void setDsValue(String dsValue) {
		this.dsValue = dsValue;
	}

	public int getDsId() {
		return dsId;
	}

	public void setDsId(int dsId) {
		this.dsId = dsId;
	}

	public int getCompId() {
		return compId;
	}

	public void setCompId(int compId) {
		this.compId = compId;
	}

}
