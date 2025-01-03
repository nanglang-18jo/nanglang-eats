package com.sparta.nanglangeats.global.common.exception;

import static com.sparta.nanglangeats.global.common.exception.ErrorCode.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<?> handleCustomException(CustomException e) {
		return ResponseEntity.status(e.getErrorCode().getHttpStatus())
			.body(new ExceptionDto(e.getErrorCode()));
	}

	@ExceptionHandler(ParameterException.class)
	public ResponseEntity<?> handleParameterException(ParameterException e) {
		List<CustomFieldError> customFieldErrors = e.getCustomFieldErrors();
		StringBuilder builder = new StringBuilder();

		for (CustomFieldError customFieldError : customFieldErrors) {
			builder.append(customFieldError.getRejectedValue()).append(" : ")
				.append(customFieldError.getErrorMessage()).append("\n");
		}
		builder.deleteCharAt(builder.length() -1);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDto(builder.toString()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleException(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		StringBuilder builder = new StringBuilder();

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			builder.append(fieldError.getField()).append(" : ")
				.append(fieldError.getDefaultMessage()).append("\n");
		}
		builder.deleteCharAt(builder.length() -1);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDto(builder.toString()));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDto(COMMON_INVALID_PARAMETER));
	}
}
