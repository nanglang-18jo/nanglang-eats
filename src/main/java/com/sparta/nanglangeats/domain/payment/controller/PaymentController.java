package com.sparta.nanglangeats.domain.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.payment.controller.request.PaymentApproveRequest;
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
/*
	// 결제 조회
	@GetMapping("/{paymentId}")
	public ResponseEntity<CommonResponse<?>> getPayment(@PathVariable Long paymentId) {
		Payment payment = paymentService.getPayment(paymentId);
		return ResponseEntity.ok(new CommonResponse<>(200, "결제 조회 성공", payment));
	}

	// 결제 업데이트
	@PatchMapping("/{paymentId}")
	public ResponseEntity<CommonResponse<?>> updatePayment(
		@PathVariable Long paymentId,
		@RequestBody Payment paymentRequest
	) {
		Payment updatedPayment = paymentService.updatePayment(paymentId, paymentRequest);
		return ResponseEntity.ok(new CommonResponse<>(200, "결제 수정 성공", updatedPayment));
	}

	// 결제 삭제
	@DeleteMapping("/{paymentId}")
	public ResponseEntity<CommonResponse<?>> deletePayment(@PathVariable Long paymentId) {
		paymentService.deletePayment(paymentId);
		return ResponseEntity.ok(new CommonResponse<>(200, "결제 삭제 성공", null));
	}
*/
}