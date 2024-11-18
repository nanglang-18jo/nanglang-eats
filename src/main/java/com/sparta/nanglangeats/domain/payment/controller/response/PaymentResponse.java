package com.sparta.nanglangeats.domain.payment.controller.response;

import com.sparta.nanglangeats.domain.payment.entity.Payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
	private Long paymentId;
	private String paymentKey;
	private String amount;
	private String status;
	private String type;
	private String orderUuid;

	public PaymentResponse(Payment payment) {
		this.paymentId = payment.getPaymentId();
		this.paymentKey = payment.getPaymentKey();
		this.amount = payment.getAmount();
		this.status = payment.getStatus().name();
		this.type = payment.getType().name();
		this.orderUuid = payment.getOrder() != null ? payment.getOrder().getOrderUuid() : null;
	}
}