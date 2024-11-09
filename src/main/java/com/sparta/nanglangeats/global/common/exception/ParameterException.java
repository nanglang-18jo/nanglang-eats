package com.sparta.nanglangeats.global.common.exception;

import java.util.List;

import lombok.Getter;

@Getter
public class ParameterException extends CustomException {

	private final List<CustomFieldError> customFieldErrors;

	public ParameterException(ErrorCode errorCode, List<CustomFieldError> customFieldErrors) {
		super(errorCode);
		this.customFieldErrors = customFieldErrors;
	}
}
