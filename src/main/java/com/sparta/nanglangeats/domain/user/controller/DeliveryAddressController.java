package com.sparta.nanglangeats.domain.user.controller;

import static com.sparta.nanglangeats.global.common.util.ControllerUtil.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.user.controller.dto.request.DeliveryAddressCreateRequest;
import com.sparta.nanglangeats.domain.user.controller.dto.request.DeliveryAddressUpdateRequest;
import com.sparta.nanglangeats.domain.user.service.DeliveryAddressService;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DeliveryAddressController {

	private final DeliveryAddressService deliveryAddressService;

	@PostMapping("/api/users/delivery-addresses")
	public ResponseEntity<CommonResponse<?>> createDeliveryAddress(@AuthenticationPrincipal User user, @RequestBody @Valid DeliveryAddressCreateRequest request) {
		return getResponseEntity(CREATED, deliveryAddressService.createDeliveryAddress(user, request), "배송 주소 등록 완료");
	}

	@GetMapping("/api/users/delivery-addresses")
	public ResponseEntity<CommonResponse<?>> getDeliveryAddressList(@AuthenticationPrincipal User user) {
		return getOkResponseEntity(deliveryAddressService.getDeliveryAddressList(user), "내 배송 주소 리스트 조회 성공");
	}

	@GetMapping("/api/users/delivery-addresses/{deliveryAddressId}")
	public ResponseEntity<CommonResponse<?>> getDeliveryAddressDetail(@AuthenticationPrincipal User user, @PathVariable Long deliveryAddressId) {
		return getOkResponseEntity(deliveryAddressService.getDeliveryAddressDetail(user, deliveryAddressId), "내 배송 주소 상세 조회 성공");
	}

	@PutMapping("/api/users/delivery-addresses/{deliveryAddressId}")
	public ResponseEntity<CommonResponse<?>> updateDeliveryAddress(@AuthenticationPrincipal User user, @PathVariable Long deliveryAddressId, @RequestBody @Valid DeliveryAddressUpdateRequest request) {
		return getOkResponseEntity(deliveryAddressService.updateDeliveryAddress(user, deliveryAddressId, request), "배송 주소 수정 완료");
	}

	@DeleteMapping("/api/users/delivery-addresses/{deliveryAddressId}")
	public ResponseEntity<CommonResponse<?>> deleteDeliveryAddress(@AuthenticationPrincipal User user, @PathVariable Long deliveryAddressId) {
		deliveryAddressService.deleteDeliveryAddress(user, deliveryAddressId);
		return getResponseEntity(NO_CONTENT, "배송 주소 삭제 완료");
	}
}