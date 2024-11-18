package com.sparta.nanglangeats.global.common.exception;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	// COMMON
	COMMON_SYSTEM_ERROR(BAD_REQUEST, "일시적인 오류입니다. 잠시 후 다시 시도해 주세요."),
	COMMON_INVALID_PARAMETER(BAD_REQUEST, "요청한 값이 올바르지 않습니다."),
	INVALID_SORTBY_PARAMETER(BAD_REQUEST, "유효하지 않은 정렬 기준입니다."),

	// JWT
	INVALID_ACCESS_TOKEN(UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다."),
	INVALID_REFRESH_TOKEN(UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다. 다시 로그인 해주세요."),

	EXPIRED_ACCESS_TOKEN(UNAUTHORIZED, "Expired JWT token, 만료된 JWT token 입니다."),

	// User
	INVALID_PASSWORD(BAD_REQUEST, "유효하지 않은 비밀번호입니다"),
	INVALID_USERNAME(BAD_REQUEST, "유효하지 않은 아이디입니다"),

	INVALID_ROLE_REQUEST(BAD_REQUEST, "유효하지 않은 권한 요청입니다"),

	DUPLICATED_USERNAME(BAD_REQUEST, "이미 존재하는 사용자 아이디입니다."),
	DUPLICATED_NICKNAME(BAD_REQUEST, "이미 존재하는 사용자 유저네임입니다."),
	DUPLICATED_EMAIL(BAD_REQUEST, "이미 존재하는 사용자 이메일입니다."),

	USER_NOT_FOUND(NOT_FOUND, "유저를 찾을 수 없습니다."),

	// DELIVERY ADDRESS
	DELIVERY_ADDRESS_NOT_FOUND(NOT_FOUND, "해당 배송 주소를 찾을 수 없습니다."),

	// Order
	ORDER_PRODUCT_QUANTITY_INVALID(BAD_REQUEST, "상품 수량은 0보다 커야 합니다."),
	ORDER_PRODUCT_ID_INVALID(BAD_REQUEST, "상품 아이디가 유효하지 않습니다."),
	ORDER_STORE_ID_INVALID(BAD_REQUEST, "가게 아이디가 유효하지 않습니다."),
	STORE_OWNER_NOT_FOUND(BAD_REQUEST, "가게 주인을 찾을 수 없거나 잘못된 유형입니다."),
	STORE_NAME_NOT_FOUND(NOT_FOUND, "가게 이름을 찾을 수 없습니다."),
	ORDER_NOT_FOUND(NOT_FOUND, "주문 정보를 찾을 수 없습니다."),
	ORDER_UPDATE_FORBIDDEN(FORBIDDEN, "해당 주문을 수정할 권한이 없습니다."),
	ORDER_CANCEL_TIME_EXCEEDED(CONFLICT, "주문 취소는 5분 이내에만 가능합니다."),
	ORDER_STATUS_CHANGE_INVALID(CONFLICT, "주문 상태를 변경할 수 없습니다."),
	ORDER_NOT_CANCELABLE(CONFLICT, "취소 가능한 주문 상태가 아닙니다."),
	ORDER_NOT_DELETABLE(CONFLICT, "삭제는 취소된(CANCELED) 주문만 가능합니다."),

	// Review
	REVIEW_NOT_FOUND(NOT_FOUND, "해당 리뷰를 찾을 수 없습니다."),

	// Address
	ADDRESS_NOT_FOUND(NOT_FOUND, "해당 주소를 찾을 수 없습니다."),
	GEOCODING_API_CALL_FAILED(INTERNAL_SERVER_ERROR, "Geocoding API 호출에 실패하였습니다."),
	IO_EXCEPTION_OCCURRED(INTERNAL_SERVER_ERROR, "서버에서 입출력 오류가 발생하였습니다."),

	// Authorization
	ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다."),

	// Category
	CATEGORY_NOT_FOUND(NOT_FOUND, "존재하지 않는 카테고리입니다."),

	// Image
	UPLOAD_FAILED(HttpStatus.GATEWAY_TIMEOUT, "이미지 업로드에 실패하였습니다."),
	UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "지원하지 않는 미디어 유형입니다."),
	IMAGE_LIMIT_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "이미지의 최대 용량은 10MB입니다."),
	IMAGE_COUNT_EXCEEDED(HttpStatus.PAYLOAD_TOO_LARGE, "사진은 최대 5개까지 등록 가능합니다"),

	// Store
	USER_ROLE_NOT_OWNER(BAD_REQUEST, "OWNER 권한을 가진 사용자만 가게 주인으로 등록할 수 있습니다."),
	STORE_NOT_FOUND(NOT_FOUND, "존재하지 않는 가게입니다."),

	// Product
	PRODUCT_NOT_FOUND(NOT_FOUND, "존재하지 않는 상품입니다."),
	PRODUCT_NOT_PUBLIC(FORBIDDEN, "해당 상품에 접근할 수 없습니다."),

	;
	private final HttpStatus httpStatus;
	private final String message;

}