package com.sparta.nanglangeats.domain.payment.service;

import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.sparta.nanglangeats.domain.order.entity.Order;
import com.sparta.nanglangeats.domain.order.repository.OrderRepository;
import com.sparta.nanglangeats.domain.payment.client.TossPaymentClient;
import com.sparta.nanglangeats.domain.payment.client.TossPaymentSimulator;
import com.sparta.nanglangeats.domain.payment.controller.request.PaymentApproveRequest;
import com.sparta.nanglangeats.domain.payment.controller.request.PaymentUpdateRequest;
import com.sparta.nanglangeats.domain.payment.controller.response.PaymentResponse;
import com.sparta.nanglangeats.domain.payment.entity.Payment;
import com.sparta.nanglangeats.domain.payment.enums.PaymentStatus;
import com.sparta.nanglangeats.domain.payment.enums.PaymentType;
import com.sparta.nanglangeats.domain.payment.repository.PaymentRepository;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.common.exception.ErrorCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;
	private final TossPaymentClient tossPaymentClient;
	private final TossPaymentSimulator tossPaymentSimulator;

	// 결제 승인 및 저장
	@Transactional
	public PaymentResponse approvePayment(@Valid PaymentApproveRequest request, User user) {

		// Toss API 호출
		//JsonNode response = tossPaymentClient.approvePayment(request.getPaymentKey(),
		//	String.valueOf(request.getOrderId()), request.getPaymentAmount());

		// Toss API 응답 시뮬레이션
		JsonNode response = tossPaymentSimulator.approvePayment(request.getPaymentKey(),
			String.valueOf(request.getOrderId()), request.getPaymentAmount());

		// 주문 조회
		Order order = null;

		try {
			// 먼저 숫자인지 확인하고 ID로 조회
			Long orderIdAsLong = Long.valueOf(request.getOrderId());
			order = orderRepository.findById(orderIdAsLong)
				.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
		} catch (NumberFormatException e) {
			// 숫자가 아니면 UUID로 조회
			order = orderRepository.findByOrderUuid(request.getOrderId())
				.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
		}
		Hibernate.initialize(order.getOrderProducts());

		// 결제 저장
		Payment payment = Payment.builder()
			.order(order)
			.paymentKey(response.get("paymentKey").asText())
			.amount(String.valueOf(response.get("totalAmount").asLong()))
			.type(PaymentType.valueOf(response.get("type").asText().toUpperCase()))
			.status(PaymentStatus.valueOf(response.get("status").asText().toUpperCase()))
			.build();

		paymentRepository.save(payment);
		return new PaymentResponse(payment);
	}

	// 결제 조회
	@Transactional(readOnly = true)
	public PaymentResponse getPaymentByPaymentId(Long paymentId) {
		Payment payment = paymentRepository.findByPaymentId(paymentId)
			.orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
		return new PaymentResponse(payment);
	}

	// 결제 수정
	@Transactional
	public PaymentResponse updatePayment(Long paymentId, @Valid PaymentUpdateRequest request) {
		Payment payment = paymentRepository.findByPaymentId(paymentId)
			.orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

		// 업데이트 로직
		if (request.getAmount() != null) {
			payment.setAmount(request.getAmount());
		}
		if (request.getStatus() != null) {
			payment.updateStatus(PaymentStatus.valueOf(request.getStatus()));
		}
		if (request.getType() != null) {
			payment.setType(request.getType());
		}
		paymentRepository.save(payment);
		return new PaymentResponse(payment);
	}

	// 결제 삭제
	@Transactional
	public void deletePayment(Long paymentId) {
		Payment payment = paymentRepository.findByPaymentId(paymentId)
			.orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
		paymentRepository.delete(payment);
	}

	// 결제 조회 (PaymentKey)
	@Transactional(readOnly = true)
	public Optional<Payment> getPaymentByPaymentKey(String paymentKey) {
		return paymentRepository.findByPaymentKey(paymentKey);
	}

	// 결제 조회 (orderId)
	@Transactional(readOnly = true)
	public Optional<Payment> getPaymentByOrderId(String orderId) {
		return paymentRepository.findByOrder_OrderUuid(orderId);
	}

	// 결제 취소
	@Transactional
	public Payment cancelPayment(String paymentKey, String cancelReason) {
		// Toss API 호출
		//tossPaymentClient.cancelPayment(paymentKey, cancelReason);

		// Toss API 시뮬레이터 호출
		tossPaymentSimulator.cancelPayment(paymentKey, cancelReason);

		// DB에서 결제 조회 및 상태 업데이트
		Payment payment = paymentRepository.findByPaymentKey(paymentKey)
			.orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
		payment.updateStatus(PaymentStatus.CANCELED);

		// 저장
		return paymentRepository.save(payment);
	}

}