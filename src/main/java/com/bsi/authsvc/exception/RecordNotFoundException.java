/**
 *
 */
package com.bsi.authsvc.exception;

import com.bsi.authsvc.util.Errors;

/**
 * @author igorV
 *
 */
public class RecordNotFoundException extends BaseException {

    private static final long serialVersionUID = -5176523280810090203L;

    public RecordNotFoundException() {
        super();

    }

    public RecordNotFoundException(int code, String message, Throwable cause) {
        super(code, message, cause);

    }

    public RecordNotFoundException(int code) {
        super(code);

    }

    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);

    }

    public RecordNotFoundException(String message) {
        super(message);

    }

    public RecordNotFoundException(Throwable cause) {
        super(cause);

    }

    public RecordNotFoundException(int code, String message) {
        super(code, message);

    }

    /**
     * @param code
     * @param message
     */
    public RecordNotFoundException(int code, String message, Object... args) {
        super(code, String.format(message, args));
    }

    /**
     * @param errorDescriptions
     */
    public RecordNotFoundException(Errors errors, Object... args) {
        this(errors.getCode(), errors.getDesc(args));
    }
}
