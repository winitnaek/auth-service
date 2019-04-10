/**
 *
 */
package com.bsi.authsvc.exception;

import com.bsi.authsvc.util.Errors;

/**
 * @author Amrutha
 *
 */
public class ProcessingException extends BaseException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ProcessingException() {
        super();

    }

    public ProcessingException(int code, String message, Throwable cause) {
        super(code, message, cause);

    }

    public ProcessingException(int code) {
        super(code);

    }

    public ProcessingException(String message, Throwable cause) {
        super(message, cause);

    }

    public ProcessingException(String message) {
        super(message);

    }

    public ProcessingException(Throwable cause) {
        super(cause);

    }

    public ProcessingException(int code, String message) {
        super(code, message);

    }

    /**
     * @param code
     * @param message
     */
    public ProcessingException(int code, String message, Object... args) {
        super(code, String.format(message, args));
    }

    /**
     * @param errorDescriptions
     */
    public ProcessingException(Errors errors, Object... args) {
        this(errors.getCode(), errors.getDesc(), args);
    }

}
