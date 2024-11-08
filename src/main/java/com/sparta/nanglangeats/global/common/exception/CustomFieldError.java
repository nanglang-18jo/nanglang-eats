package com.sparta.nanglangeats.global.common.exception;

import lombok.Getter;

@Getter
public class CustomFieldError {

	private final String rejectedValue;
	private final String errorMessage;

	public CustomFieldError(String rejectedValue, ErrorCode errorCode) {
		this.rejectedValue = rejectedValue;
		this.errorMessage = errorCode.getMessage();
	}
}
