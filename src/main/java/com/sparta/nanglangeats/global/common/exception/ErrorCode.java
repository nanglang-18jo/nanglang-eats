package com.sparta.nanglangeats.global.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // COMMON
    COMMON_INVALID_PARAMETER(BAD_REQUEST, "요청한 값이 올바르지 않습니다."),

    // JWT
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다. 다시 로그인 해주세요."),

    // User
    INVALID_PASSWORD(BAD_REQUEST, "유효하지 않은 비밀번호입니다"),
    INVALID_USERNAME(BAD_REQUEST, "유효하지 않은 아이디입니다"),

    DUPLICATED_USERNAME(BAD_REQUEST, "이미 존재하는 사용자 아이디입니다."),
    DUPLICATED_NICKNAME(BAD_REQUEST, "이미 존재하는 사용자 유저네임입니다."),
    DUPLICATED_EMAIL(BAD_REQUEST, "이미 존재하는 사용자 이메일입니다."),

    USER_NOT_FOUND(NOT_FOUND, "유저를 찾을 수 없습니다."),
    // Order

    // Review


    ;
    private final HttpStatus httpStatus;
    private final String message;

}