package com.sparta.nanglangeats.domain.payment.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.payment.controller.request.PaymentApproveRequest;
import com.sparta.nanglangeats.domain.payment.controller.request.PaymentUpdateRequest;
import com.sparta.nanglangeats.domain.payment.service.PaymentService;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;
import com.sparta.nanglangeats.global.common.util.ControllerUtil;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

	private final PaymentService paymentService;

	// 결제 승인 및 저장
	@PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER')")
	@PostMapping
	public ResponseEntity<CommonResponse<?>> approvePayment(
		@AuthenticationPrincipal User user,
		@RequestBody @Valid PaymentApproveRequest request) {
		return ControllerUtil.getResponseEntity(HttpStatus.CREATED,
			paymentService.approvePayment(request, user),
			"결제 승인 완료");
	}

	// 결제 조회
	@PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER', 'MANAGER')")
	@GetMapping("/{paymentId}")
	public ResponseEntity<CommonResponse<?>> getPaymentByUuid(
		@PathVariable Long paymentId,
		@AuthenticationPrincipal User user) {
		return ControllerUtil.getResponseEntity(HttpStatus.OK,
			paymentService.getPaymentByPaymentId(paymentId),
			"결제 조회 성공");
	}

	// 결제 수정
	@PreAuthorize("hasAnyRole('MANAGER')")
	@PatchMapping("/{paymentId}")
	public ResponseEntity<CommonResponse<?>> updatePayment(
		@AuthenticationPrincipal User user,
		@PathVariable Long paymentId,
		@RequestBody @Valid PaymentUpdateRequest request) {
		return ControllerUtil.getResponseEntity(HttpStatus.OK,
			paymentService.updatePayment(paymentId, request),
			"결제 수정 성공");
	}

	// 결제 삭제
	@PreAuthorize("hasAnyRole('MANAGER')")
	@DeleteMapping("/{paymentId}")
	public ResponseEntity<CommonResponse<?>> deletePayment(
		@AuthenticationPrincipal User user,
		@PathVariable Long paymentId) {
		paymentService.deletePayment(paymentId);
		return ControllerUtil.getResponseEntity(HttpStatus.NO_CONTENT, null, "결제 삭제 성공");
	}
}
