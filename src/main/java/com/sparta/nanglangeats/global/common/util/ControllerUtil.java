package com.sparta.nanglangeats.global.common.util;

import com.sparta.nanglangeats.global.common.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public final class ControllerUtil {

    public static ResponseEntity<CommonResponse<?>> getOkResponseEntity(String msg) {
        return getResponseEntity(HttpStatus.OK, null, msg);
    }

    public static ResponseEntity<CommonResponse<?>> getOkResponseEntity(Object response, String msg) {
        return getResponseEntity(HttpStatus.OK, response, msg);
    }

    public static ResponseEntity<CommonResponse<?>> getResponseEntity(HttpStatus status, String msg) {
        return getResponseEntity(status, null, msg);
    }

    public static ResponseEntity<CommonResponse<?>> getResponseEntity(HttpStatus status, Object response, String msg) {
        return ResponseEntity.status(status).body(CommonResponse.builder()
            .statusCode(status.value())
            .msg(msg)
            .data(response)
            .build());
    }

    public static void verifyPathIdWithBody(Long pathId, Long bodyId) {
        if (!pathId.equals(bodyId)) {
            throw new IllegalArgumentException("PathVariable의 Id가 RequestBody의 Id와 일치하지 않습니다.");
        }
    }
}