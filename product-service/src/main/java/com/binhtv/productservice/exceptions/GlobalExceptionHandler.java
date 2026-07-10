package com.binhtv.productservice.exceptions;

import com.binhtv.productservice.model.dto.ApiResponse;
import com.binhtv.productservice.model.dto.ValidationErrorDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<ValidationErrorDto>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {

        final List<ValidationErrorDto> errors = exception.getBindingResult().getFieldErrors().stream()
                .map(this::mapValidationError)
                .toList();
        final ApiResponse<List<ValidationErrorDto>> response = new ApiResponse<>(
                "Validation failed!",
                HttpStatus.BAD_REQUEST.value(),
                errors);

        log.warn("Validation errors: {}, parameters: {}", errors, exception.getBindingResult().getTarget());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<ValidationErrorDto>> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {

        final ValidationErrorDto error = ValidationErrorDto.builder()
                .field(exception.getName())
                .message("Invalid value for parameter '%s'.".formatted(exception.getName()))
                .rejectedValue(exception.getValue())
                .build();
        final ApiResponse<ValidationErrorDto> response = new ApiResponse<>(
                "Invalid request parameter!",
                HttpStatus.BAD_REQUEST.value(),
                error);

        log.warn("Invalid request parameter: {}={}", exception.getName(), exception.getValue());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception) {

        log.warn("Request body could not be read: {}", exception.getMessage());

        return buildResponse("Request body is invalid or malformed.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EntityNotFoundException.class, NoSuchElementException.class})
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(RuntimeException exception) {

        return buildResponse(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException exception) {

        return buildResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(
            DataIntegrityViolationException exception) {

        log.warn("Data integrity violation", exception);

        return buildResponse("Data violates database constraints.", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleResponseStatusException(ResponseStatusException exception) {

        final HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
        final String message = exception.getReason() == null ? status.getReasonPhrase() : exception.getReason();

        return buildResponse(message, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {

        log.error("Unexpected product-service error", exception);

        return buildResponse("Internal server error.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ValidationErrorDto mapValidationError(FieldError fieldError) {
        return ValidationErrorDto.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .build();
    }

    private ResponseEntity<ApiResponse<Void>> buildResponse(String message, HttpStatus status) {

        final ApiResponse<Void> response = new ApiResponse<>(message, status.value(), null);

        return ResponseEntity.status(status).body(response);
    }
}