package com.bsi.sec.saml.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Configurations {
	private boolean enabled;
	private boolean testPageEnabled;
	private  BSIConfiguration[] configs;

	public Configurations(){}

	/**
	 * @return the enabled
	 */
	@XmlElement(required=true, defaultValue="true")
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the testPageEnabled
	 */
	@XmlElement(required=true, defaultValue="true")
	public boolean isTestPageEnabled() {
		return testPageEnabled;
	}

	/**
	 * @param testPageEnabled the testPageEnabled to set
	 */
	public void setTestPageEnabled(boolean testPageEnabled) {
		this.testPageEnabled = testPageEnabled;
	}

	/**
	 * @return the configs
	 */
	@XmlElement(required=true,nillable=false, name="config")
	public BSIConfiguration[] getConfigs() {
		return configs;
	}

	/**
	 * @param configs the configs to set
	 */
	public void setConfigs(BSIConfiguration[] configs) {
		this.configs = configs;
	}
	//
	public boolean hasConfigurations()
	{		
		if(configs != null && configs.length > 0)
			return true;
		else
			return false;
	}
	//
	
}
