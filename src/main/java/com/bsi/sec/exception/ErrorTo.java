/**
 *
 */
package com.bsi.sec.exception;

import java.io.Serializable;
import java.util.Objects;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author igorV
 *
 */
@XmlRootElement
public class ErrorTo implements Serializable {

    private static final long serialVersionUID = -4500626205001964124L;

    private int code;
    private String message;
    private String description;

    public ErrorTo() {
    }

    public ErrorTo(String message) {
        this(message, null);
    }

    public ErrorTo(String message, String description) {
        this.message = message;
        this.description = description;
    }

    public ErrorTo(int code, String message) {
        this(message);
        this.code = code;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }
        if (!(o instanceof ErrorTo)) {
            return false;
        }

        ErrorTo errorTo = (ErrorTo) o;
        return code == errorTo.code
                && Objects.equals(message, errorTo.message)
                && Objects.equals(description, errorTo.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, description);
    }

}
