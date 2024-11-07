package com.sparta.nanglangeats.global.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // JWT
    INVALID_REFRESH_TOKEN(UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다. 다시 로그인 해주세요."),
    CARD_ACCESS_FORBIDDEN(FORBIDDEN, "카드 작성자 및 매니저만 접근할 수 있습니다."),

    // User
    DUPLICATED_USERNAME(BAD_REQUEST, "이미 존재하는 사용자 아이디입니다."),

    // Order

    // Review


    ;
    private final HttpStatus httpStatus;
    private final String message;

}