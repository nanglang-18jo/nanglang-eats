package com.sparta.nanglangeats.domain.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.order.controller.dto.request.OrderCreateRequest;
import com.sparta.nanglangeats.domain.order.controller.dto.request.OrderStatusUpdateRequest;
import com.sparta.nanglangeats.domain.order.controller.dto.request.OrderUpdateRequest;
import com.sparta.nanglangeats.domain.order.controller.dto.response.OrderStatusUpdateResponse;
import com.sparta.nanglangeats.domain.order.service.OrderService;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;
import com.sparta.nanglangeats.global.common.util.ControllerUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/orders")
public class OrderController {

	private final OrderService orderService;

	@PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER')")
	@PostMapping
	public ResponseEntity<CommonResponse<?>> createOrder(
		@AuthenticationPrincipal User user,
		@Valid @RequestBody OrderCreateRequest request) {

		return ControllerUtil.getResponseEntity(HttpStatus.CREATED, orderService.createOrder(request, user),
			"주문 등록 완료");
	}

	@PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER')")
	@PatchMapping("/{orderId}")
	public ResponseEntity<CommonResponse<?>> updateOrder(
		@PathVariable Long orderId,
		@AuthenticationPrincipal User user,
		@Valid @RequestBody OrderUpdateRequest request) {

		return ControllerUtil.getResponseEntity(HttpStatus.OK, orderService.updateOrder(orderId, request, user),
			"주문 정보 수정 완료");
	}

	@PreAuthorize("hasRole('OWNER')")
	@PatchMapping("/{orderId}/status")
	public ResponseEntity<CommonResponse<?>> updateOrderStatus(
		@AuthenticationPrincipal User user,
		@PathVariable Long orderId,
		@RequestBody @Valid OrderStatusUpdateRequest request) {

		orderService.updateOrderStatus(orderId, user, request.getStatus());
		return ControllerUtil.getResponseEntity(HttpStatus.OK, new OrderStatusUpdateResponse(orderId), "주문 상태 수정 완료");
	}
}