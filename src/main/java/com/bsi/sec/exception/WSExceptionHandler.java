/**
 *
 */
package com.bsi.sec.exception;

import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * Place Spring exception handlers here!
 *
 * @author igorV
 *
 */
@ControllerAdvice
public class WSExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(FORBIDDEN)
    @ResponseBody
    public ErrorTo handleAccessDeniedException(AccessDeniedException exception) {
        return new ErrorTo(FORBIDDEN.value(), exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ErrorTo processMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        return new ErrorTo(METHOD_NOT_ALLOWED.value(), exception.getMessage());
    }

    @ExceptionHandler(InvalidUserException.class)
    @ResponseStatus(UNAUTHORIZED)
    @ResponseBody
    public ErrorTo handleInvalidUserException(InvalidUserException exception) {
        return new ErrorTo(UNAUTHORIZED.value(), exception.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorTo handleMethodArgumentNotValidException(MissingServletRequestParameterException exception) {
        return new ErrorTo(BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Set<ErrorTo> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> new ErrorTo(HttpStatus.BAD_REQUEST.value(), e.getDefaultMessage()))
                .collect(Collectors.toSet());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Set<ErrorTo> handleConstraintViolationException(ConstraintViolationException exception) {
        return exception.getConstraintViolations()
                .stream()
                .map(cv -> new ErrorTo(HttpStatus.BAD_REQUEST.value(), cv.getMessage()))
                .collect(Collectors.toSet());
    }

    @ExceptionHandler(BadParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorTo handleBadParameterException(BadParameterException exception) {
        return new ErrorTo(exception.getCode(), "Bad parameter specified! -> " + exception.getMessage());
    }

    @ExceptionHandler(BadStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorTo handleBadStateException(BadStateException exception) {
        return new ErrorTo(exception.getCode(), "Bad data state! -> " + exception.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorTo handleValidationException(ValidationException exception) {
        return new ErrorTo(exception.getCode(), "Validation Error! -> " + exception.getMessage());
    }

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorTo handleRecordNotFoundException(RecordNotFoundException exception) {
        return new ErrorTo(exception.getCode(), "Record not found! -> " + exception.getMessage());
    }

    @ExceptionHandler(ConfigurationException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_REQUIRED)
    @ResponseBody
    public ErrorTo handleConfigurationException(ConfigurationException exception) {
        return new ErrorTo(exception.getCode(), "Configuration error! -> " + exception.getMessage());
    }

    @ExceptionHandler(ProcessingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorTo handleProcessingException(ProcessingException exception) {
        return new ErrorTo(exception.getCode(), "Unknown processing error! -> " + exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorTo> handleException(Exception ex) {
        BodyBuilder builder;
        ErrorTo errorTo;

        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(
                ex.getClass(), ResponseStatus.class);

        if (responseStatus != null) {
            builder = ResponseEntity.status(responseStatus.value());
            int code = responseStatus.code() != null ? responseStatus.code()
                    .value() : -1;
            errorTo = new ErrorTo(code, "Operation failed! -> "
                    + responseStatus.reason());
        } else {
            builder = ResponseEntity.status(INTERNAL_SERVER_ERROR);
            errorTo = new ErrorTo(INTERNAL_SERVER_ERROR.value(),
                    ex.getMessage());
        }

        return builder.body(errorTo);
    }
}
