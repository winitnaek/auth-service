package com.bsi.sec.saml.exception;

public class ConfigurationException extends Exception {
	public ConfigurationException(String msg, Throwable th)
	{
		super(msg, th);
	}
	
	public ConfigurationException(Throwable th)
	{
		super(th);
	}
	public ConfigurationException(String msg)
	{
		super(msg);
	}
}
