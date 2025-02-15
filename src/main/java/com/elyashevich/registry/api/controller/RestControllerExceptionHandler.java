package com.elyashevich.registry.api.controller;

import com.elyashevich.registry.api.dto.ExceptionBody;
import com.elyashevich.registry.exception.InvalidTokenException;
import com.elyashevich.registry.exception.PasswordMismatchException;
import com.elyashevich.registry.exception.ResourceAlreadyExistException;
import com.elyashevich.registry.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {

    private static final String NOT_FOUND_MESSAGE = "Resource was not found.";
    private static final String ALREADY_EXISTS_MESSAGE = "Resource already exists.";
    private static final String NOT_SUPPORTED_MESSAGE = "Http method with this URL not found.";
    private static final String FAILED_VALIDATION_MESSAGE = "Validation failed.";
    private static final String UNEXPECTED_ERROR_MESSAGE = "Something went wrong.";
    private static final String PASSWORD_MISMATCH_MESSAGE = "Password mismatch.";
    private static final String TOKEN_INVALID_MESSAGE = "Invalid token.";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionBody> handleNotFound(final ResourceNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.handleException(exception, NOT_FOUND_MESSAGE));
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<ExceptionBody> handleAlreadyExists(final ResourceAlreadyExistException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.handleException(exception, ALREADY_EXISTS_MESSAGE));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ExceptionBody> handleException(final HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this.handleException(exception, NOT_SUPPORTED_MESSAGE));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ExceptionBody> handlePasswordMismatchException(final PasswordMismatchException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this.handleException(exception, PASSWORD_MISMATCH_MESSAGE));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionBody> handleInvalidTokenException(final InvalidTokenException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(this.handleException(exception, TOKEN_INVALID_MESSAGE));
    }

    @SuppressWarnings("all")
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionBody> handleValidation(final MethodArgumentNotValidException exception) {
        var errors = exception.getBindingResult()
                .getFieldErrors().stream()
                .collect(Collectors.toMap(
                                FieldError::getField,
                                fieldError -> fieldError.getDefaultMessage(),
                                (exist, newMessage) -> exist + " " + newMessage
                        )
                );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionBody(FAILED_VALIDATION_MESSAGE, errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionBody> handleException(final Exception exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this.handleException(exception, UNEXPECTED_ERROR_MESSAGE));
    }

    private ExceptionBody handleException(final Exception exception, final String defaultMessage) {
        var message = exception.getMessage() == null ? defaultMessage : exception.getMessage();
        exception.printStackTrace();
        log.warn("{} '{}'.", defaultMessage, message);
        return new ExceptionBody(message);
    }
}
