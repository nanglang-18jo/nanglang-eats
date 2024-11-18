package com.sparta.nanglangeats.domain.order.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	// 주문 등록
	@PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER')")
	@PostMapping
	public ResponseEntity<CommonResponse<?>> createOrder(
		@AuthenticationPrincipal User user,
		@RequestBody @Valid OrderCreateRequest request) {

		return ControllerUtil.getResponseEntity(HttpStatus.CREATED, orderService.createOrder(request, user),
			"주문 등록 완료");
	}

	// 주문 수정
	@PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER')")
	@PatchMapping("/{orderId}")
	public ResponseEntity<CommonResponse<?>> updateOrder(
		@PathVariable Long orderId,
		@AuthenticationPrincipal User user,
		@RequestBody @Valid OrderUpdateRequest request) {

		return ControllerUtil.getResponseEntity(HttpStatus.OK, orderService.updateOrder(orderId, request, user),
			"주문 정보 수정 완료");
	}

	// 주문 상태 수정
	@PreAuthorize("hasRole('OWNER')")
	@PatchMapping("/{orderId}/status")
	public ResponseEntity<CommonResponse<?>> updateOrderStatus(
		@AuthenticationPrincipal User user,
		@PathVariable Long orderId,
		@RequestBody @Valid OrderStatusUpdateRequest request) {

		orderService.updateOrderStatus(orderId, user, request.getStatus());
		return ControllerUtil.getResponseEntity(HttpStatus.OK, new OrderStatusUpdateResponse(orderId), "주문 상태 수정 완료");
	}

	// 주문 취소
	@PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER')")
	@PatchMapping("/{orderId}/cancel")
	public ResponseEntity<CommonResponse<?>> cancelOrder(
		@PathVariable Long orderId,
		@AuthenticationPrincipal User user) {

		orderService.cancelOrder(orderId, user);

		return ControllerUtil.getResponseEntity(HttpStatus.OK, orderId, "주문 취소 완료");
	}

	// 주문 삭제
	@PreAuthorize("hasRole('MANAGER')")
	@DeleteMapping("/{orderId}")
	public ResponseEntity<CommonResponse<?>> deleteOrder(
		@PathVariable Long orderId,
		@AuthenticationPrincipal User user) {

		orderService.deleteOrder(orderId, user);
		return ControllerUtil.getResponseEntity(HttpStatus.OK, null, "주문 삭제 완료");
	}

	// 주문 상세 조회
	@PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER')")
	@GetMapping("/{orderId}")
	public ResponseEntity<CommonResponse<?>> getOrderDetail(
		@PathVariable Long orderId,
		@AuthenticationPrincipal User user) {

		return ControllerUtil.getResponseEntity(HttpStatus.OK, orderService.getOrderDetail(orderId, user), "주문 조회 성공");
	}

	// 주문 목록 조회
	@PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER')")
	@GetMapping
	public ResponseEntity<CommonResponse<?>> getOrderList(
		@AuthenticationPrincipal User user,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size,
		@RequestParam(defaultValue = "createdAt") String sortBy,
		@RequestParam(required = false) String status,
		@RequestParam(required = false) String search) {

		return ControllerUtil.getResponseEntity(HttpStatus.OK,
			orderService.getOrderList(user, page, size, sortBy, status, search),
			"주문 목록 조회 성공");
	}
}