/**
 *
 */
package com.bsi.sec.exception;

import com.bsi.sec.util.Errors;

/**
 * @author igorv
 *
 */
public class ValidationException extends BaseException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ValidationException() {
        super();

    }

    public ValidationException(int code, String message, Throwable cause) {
        super(code, message, cause);

    }

    public ValidationException(int code, String message) {
        super(code, message);

    }

    public ValidationException(int code) {
        super(code);

    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);

    }

    public ValidationException(String message) {
        super(message);

    }

    public ValidationException(Throwable cause) {
        super(cause);

    }

    /**
     * @param badCompany
     */
    public ValidationException(Errors errors) {
        this(errors.getCode(), errors.getDesc());
    }

    /**
     * @param code
     * @param message
     * @param args
     */
    public ValidationException(int code, String message, Object... args) {
        super(code, String.format(message, args));
    }

    /**
     * @param errorDescriptions
     * @param args
     */
    public ValidationException(Errors errors, Object... args) {
        this(errors.getCode(), errors.getDesc(args));
    }
}
