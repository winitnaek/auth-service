package com.bsi.sec.saml.exception;

@SuppressWarnings("serial")
public class UnknownUserException extends Exception {
	
	public UnknownUserException(String userName)
	{
		super("Unknow user: " + userName);
	}
	
	public UnknownUserException(String userName, Throwable th)
	{
		super("Unknow user: " + userName, th);
	}
	
}
