package com.sparta.nanglangeats.domain.delivery_address.controller;

import static com.sparta.nanglangeats.global.common.util.ControllerUtil.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.delivery_address.controller.dto.request.DeliveryAddressCreateRequest;
import com.sparta.nanglangeats.domain.delivery_address.service.DeliveryAddressService;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DeliveryAddressController {

	private final DeliveryAddressService deliveryAddressService;

	@PostMapping("/api/delivery-address")
	public ResponseEntity<CommonResponse<?>> createDeliveryAddress(@AuthenticationPrincipal User user, @RequestBody @Valid DeliveryAddressCreateRequest request) {
		return getResponseEntity(CREATED, deliveryAddressService.createDeliveryAddress(user, request), "배송 주소 등록 완료");
	}
}