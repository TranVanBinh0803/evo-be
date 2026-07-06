package com.binhtv.authservice.exceptions;

import com.binhtv.authservice.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(RegistrationException.class)
	ResponseEntity<ApiResponse<Void>> handleRegistrationException(RegistrationException exception) {

		return buildResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(BadCredentialsException.class)
	ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(BadCredentialsException exception) {

		return buildResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	ResponseEntity<ApiResponse<List<String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

		final List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		final List<String> errorList = fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
		final ApiResponse<List<String>> response = new ApiResponse<>("Validation failed!", HttpStatus.BAD_REQUEST.value(), errorList);

		log.warn("Validation errors : {} , Parameters : {}", errorList, exception.getBindingResult().getTarget());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	private ResponseEntity<ApiResponse<Void>> buildResponse(String message, HttpStatus status) {

		final ApiResponse<Void> response = new ApiResponse<>(message, status.value(), null);

		return ResponseEntity.status(status).body(response);
	}

}
