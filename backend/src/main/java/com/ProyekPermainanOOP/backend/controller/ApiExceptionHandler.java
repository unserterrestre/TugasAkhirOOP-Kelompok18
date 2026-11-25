package com.ProyekPermainanOOP.backend.controller;

import com.ProyekPermainanOOP.backend.dto.ApiError;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> invalidFields(MethodArgumentNotValidException error) {
        String fields = String.join(", ", error.getBindingResult().getFieldErrors().stream()
                .map(field -> field.getField().equals("passwordWithinBcryptLimit") ? "password" : field.getField())
                .distinct().sorted().toList());
        return ResponseEntity.badRequest().body(new ApiError("invalid_request", "Invalid fields: " + fields));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    ResponseEntity<ApiError> invalidRequest() {
        return ResponseEntity.badRequest().body(new ApiError("invalid_request", "Invalid request body or header."));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    ResponseEntity<ApiError> unsupportedContentType() {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ApiError("unsupported_media_type", "Send an application/json request body."));
    }

    @ExceptionHandler(ResponseStatusException.class)
    ResponseEntity<ApiError> requestConflict(ResponseStatusException error) {
        return ResponseEntity.status(error.getStatusCode()).body(new ApiError("conflict", error.getReason()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    ResponseEntity<ApiError> accountUnavailable() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .header("WWW-Authenticate", "Basic realm=\"Pusaka\", charset=\"UTF-8\"")
                .body(new ApiError("unauthorized", "Valid credentials are required."));
    }

    @ExceptionHandler(DataAccessException.class)
    ResponseEntity<ApiError> databaseUnavailable() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ApiError("database_unavailable", "The database operation could not be completed."));
    }
}
