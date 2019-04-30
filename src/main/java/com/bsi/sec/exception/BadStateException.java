package com.bsi.sec.exception;

import com.bsi.sec.util.Errors;

public class BadStateException extends BaseException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public BadStateException() {
        super();

    }

    public BadStateException(int code, String message, Throwable cause) {
        super(code, message, cause);

    }

    public BadStateException(int code, String message) {
        super(code, message);

    }

    public BadStateException(int code) {
        super(code);

    }

    public BadStateException(String message, Throwable cause) {
        super(message, cause);

    }

    public BadStateException(String message) {
        super(message);

    }

    public BadStateException(Throwable cause) {
        super(cause);

    }

    /**
     * @param errors
     */
    public BadStateException(Errors errors, Object... args) {
        this(errors.getCode(), errors.getDesc(args));
    }

}
