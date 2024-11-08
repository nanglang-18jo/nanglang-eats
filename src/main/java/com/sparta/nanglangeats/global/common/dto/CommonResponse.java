package com.sparta.nanglangeats.global.common.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse<T> {

    private int statusCode;
    private String msg;
    private T data;
}
