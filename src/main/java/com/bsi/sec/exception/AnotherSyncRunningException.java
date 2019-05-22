/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.exception;

import com.bsi.sec.util.Errors;

/**
 *
 * @author igorV
 */
public final class AnotherSyncRunningException extends BaseException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AnotherSyncRunningException() {
        super();

    }

    public AnotherSyncRunningException(int code, String message, Throwable cause) {
        super(code, message, cause);

    }

    public AnotherSyncRunningException(int code, String message) {
        super(code, message);

    }

    public AnotherSyncRunningException(int code) {
        super(code);

    }

    public AnotherSyncRunningException(String message, Throwable cause) {
        super(message, cause);

    }

    public AnotherSyncRunningException(String message) {
        super(message);

    }

    public AnotherSyncRunningException(Throwable cause) {
        super(cause);

    }

    /**
     * @param badCompany
     */
    public AnotherSyncRunningException(Errors errors) {
        this(errors.getCode(), errors.getDesc());
    }

    /**
     * @param code
     * @param message
     * @param args
     */
    public AnotherSyncRunningException(int code, String message, Object... args) {
        super(code, String.format(message, args));
    }

    /**
     * @param errorDescriptions
     * @param args
     */
    public AnotherSyncRunningException(Errors errors, Object... args) {
        this(errors.getCode(), errors.getDesc(args));
    }
}
