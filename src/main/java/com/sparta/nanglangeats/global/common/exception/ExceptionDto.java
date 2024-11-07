package com.sparta.nanglangeats.global.common.exception;

import lombok.Getter;

@Getter
public class ExceptionDto {

    private ErrorCode errorCode;
    private final String message;

    public ExceptionDto(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public ExceptionDto(String message) {
        this.message = message;
    }
}