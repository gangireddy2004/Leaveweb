package com.leaveweb.exception;

import java.time.Instant;
import java.util.stream.Collectors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    record ErrorResponse(Instant timestamp, int status, String message, String path) {}

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ErrorResponse> notFound(ResourceNotFoundException ex, HttpServletRequest request) { return response(HttpStatus.NOT_FOUND, ex.getMessage(), request); }

    @ExceptionHandler({BusinessException.class, IllegalArgumentException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
    ResponseEntity<ErrorResponse> badRequest(Exception ex, HttpServletRequest request) {
        String message = ex instanceof MethodArgumentNotValidException validation
                ? validation.getBindingResult().getFieldErrors().stream().map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.joining(", "))
                : ex.getMessage();
        return response(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ErrorResponse> unauthorized(BadCredentialsException ex, HttpServletRequest request) { return response(HttpStatus.UNAUTHORIZED, "Invalid email or password", request); }

    @ExceptionHandler(DisabledException.class)
    ResponseEntity<ErrorResponse> disabled(DisabledException ex, HttpServletRequest request) { return response(HttpStatus.UNAUTHORIZED, "Your account is pending approval or inactive", request); }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> internal(Exception ex, HttpServletRequest request) { return response(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request); }

    private ResponseEntity<ErrorResponse> response(HttpStatus status, String message, HttpServletRequest request) { return ResponseEntity.status(status).body(new ErrorResponse(Instant.now(), status.value(), message, request.getRequestURI())); }
}