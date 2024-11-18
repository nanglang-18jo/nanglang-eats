package com.sparta.nanglangeats.domain.payment.client;

import com.fasterxml.jackson.databind.JsonNode;

public interface TossPayment {

	JsonNode approvePayment(String paymentKey, String orderId, Long amount);

	JsonNode getPaymentByPaymentKey(String paymentKey);

	JsonNode getPaymentByOrderId(String orderId);

	JsonNode cancelPayment(String paymentKey, String cancelReason);
}
