/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.authsvc.exception;

import com.bsi.authsvc.util.Errors;


/**
 *
 * @author igorV
 */
public class ConfigurationException extends BaseException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ConfigurationException() {
        super();

    }

    public ConfigurationException(int code, String message, Throwable cause) {
        super(code, message, cause);

    }

    public ConfigurationException(int code, String message) {
        super(code, message);

    }

    public ConfigurationException(int code) {
        super(code);

    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);

    }

    public ConfigurationException(String message) {
        super(message);

    }

    public ConfigurationException(Throwable cause) {
        super(cause);

    }

    /**
     * @param badCompany
     */
    public ConfigurationException(Errors errors) {
        this(errors.getCode(), errors.getDesc());
    }

    /**
     * @param code
     * @param message
     * @param args
     */
    public ConfigurationException(int code, String message, Object... args) {
        super(code, String.format(message, args));
    }

    /**
     * @param errorDescriptions
     * @param args
     */
    public ConfigurationException(Errors errors, Object... args) {
        this(errors.getCode(), errors.getDesc(args));
    }
}
