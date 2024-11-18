package com.sparta.nanglangeats.domain.payment.service;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nanglangeats.domain.order.entity.Order;
import com.sparta.nanglangeats.domain.order.repository.OrderRepository;
import com.sparta.nanglangeats.domain.payment.client.TossPaymentClient;
import com.sparta.nanglangeats.domain.payment.controller.request.PaymentApproveRequest;
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

	// 결제 승인 및 저장
	@Transactional
	public Payment approvePayment(@Valid PaymentApproveRequest request, User user) {

		// Toss API 호출
		//JsonNode response = tossPaymentClient.approvePayment(request.getPaymentKey(),
		//	String.valueOf(request.getOrderId()), request.getPaymentAmount());

		// 샘플 데이터를 사용하여 Toss API 응답 시뮬레이션
		JsonNode response = simulateTossPaymentResponse();

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

		return paymentRepository.save(payment);
	}
/*
	// 결제 조회 (PaymentKey)
	@Transactional(readOnly = true)
	public Optional<Payment> getPaymentByPaymentKey(String paymentKey) {
		return paymentRepository.findByPaymentKey(paymentKey);
	}

	// 결제 조회 (OrderId)
	@Transactional(readOnly = true)
	public Optional<Payment> getPaymentByOrderId(String orderId) {
		return paymentRepository.findByOrder_OrderUuid(orderId);
	}

	// 결제 취소
	@Transactional
	public Payment cancelPayment(String paymentKey, String cancelReason) {
		// Toss API 호출
		tossPaymentClient.cancelPayment(paymentKey, cancelReason);

		// DB에서 결제 조회 및 상태 업데이트
		Payment payment = paymentRepository.findByPaymentKey(paymentKey)
			.orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
		payment.updateStatus(PaymentStatus.CANCELED);

		return paymentRepository.save(payment);
	}
 */

	// Toss 결제 승인 API 응답 샘플 데이터 반환
	private JsonNode simulateTossPaymentResponse() {
		ObjectMapper objectMapper = new ObjectMapper();
		String sampleResponse = """
			{
			    "mId": "tosspayments",
			    "version": "2022-11-16",
			    "paymentKey": "B1d9edx08u7ic9yQqcTzj",
			    "status": "DONE",
			    "lastTransactionKey": "Wgz12DHTz2PaVxm5LUO6i",
			    "method": "카드",
			    "orderId": "chdimFOf9tnXV5u8Xqtlo",
			    "orderName": "토스 티셔츠 외 2건",
			    "requestedAt": "2022-06-08T15:40:09+09:00",
			    "approvedAt": "2022-06-08T15:40:49+09:00",
			    "useEscrow": false,
			    "cultureExpense": false,
			    "card": {
			        "issuerCode": "61",
			        "acquirerCode": "31",
			        "number": "12345678****789*",
			        "installmentPlanMonths": 0,
			        "isInterestFree": false,
			        "interestPayer": null,
			        "approveNo": "00000000",
			        "useCardPoint": false,
			        "cardType": "신용",
			        "ownerType": "개인",
			        "acquireStatus": "READY",
			        "amount": 15000
			    },
			    "virtualAccount": null,
			    "transfer": null,
			    "mobilePhone": null,
			    "giftCertificate": null,
			    "cashReceipt": null,
			    "cashReceipts": null,
			    "discount": null,
			    "cancels": null,
			    "secret": null,
			    "type": "NORMAL",
			    "easyPay": null,
			    "country": "KR",
			    "failure": null,
			    "isPartialCancelable": true,
			    "receipt": {
			        "url": "https://dashboard.tosspayments.com/sales-slip?transactionId=KAgfjGxIqVVXDxOiSW1wUnRWBS1dszn3DKcuhpm7mQlKP0iOdgPCKmwEdYglIHX&ref=PX"
			    },
			    "checkout": {
			        "url": "https://api.tosspayments.com/v1/payments/B1d9edx08u7ic9yQqcTzj/checkout"
			    },
			    "currency": "KRW",
			    "totalAmount": 15000,
			    "balanceAmount": 15000,
			    "suppliedAmount": 13636,
			    "vat": 1364,
			    "taxFreeAmount": 0
			}
			""";

		try {
			return objectMapper.readTree(sampleResponse);
		} catch (Exception ex) {
			throw new RuntimeException("샘플 데이터를 로드하는 중 오류 발생", ex);
		}
	}
}