package com.bsi.authsvc.exception;

import com.bsi.authsvc.util.Errors;

/**
 * @author igorv
 *
 */
public class BadParameterException extends BaseException {

    private static final long serialVersionUID = -609768636486818264L;

    public BadParameterException() {
        super();

    }

    /**
     * @param code
     * @param message
     * @param cause
     */
    public BadParameterException(int code, String message, Throwable cause) {
        super(code, message, cause);

    }

    /**
     * @param code
     * @param message
     */
    public BadParameterException(int code, String message) {
        super(code, message);

    }

    /**
     * @param code
     */
    public BadParameterException(int code) {
        super(code);

    }

    /**
     * @param message
     * @param cause
     */
    public BadParameterException(String message, Throwable cause) {
        super(message, cause);

    }

    /**
     * @param message
     */
    public BadParameterException(String message) {
        super(message);

    }

    /**
     * @param cause
     */
    public BadParameterException(Throwable cause) {
        super(cause);

    }

    /**
     * @param code
     * @param message
     * @param args
     */
    public BadParameterException(int code, String message, Object... args) {
        super(code, String.format(message, args));
    }

    /**
     * @param errors
     */
    public BadParameterException(Errors errors, Object... args) {
        this(errors.getCode(), errors.getDesc(args));
    }
}
