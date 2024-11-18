package com.sparta.nanglangeats.domain.payment.client;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class TossPaymentSimulator implements TossPayment {

	private final ObjectMapper objectMapper;

	public TossPaymentSimulator() {
		this.objectMapper = new ObjectMapper();
	}

	// 결제 승인 시뮬레이션
	@Override
	public JsonNode approvePayment(String paymentKey, String orderId, Long amount) {
		ObjectNode response = objectMapper.createObjectNode();
		response.put("mId", "tosspayments");
		response.put("version", "2022-11-16");
		response.put("paymentKey", paymentKey);
		response.put("status", "DONE");
		response.put("lastTransactionKey", "mockTransactionKey123");
		response.put("method", "카드");
		response.put("orderId", orderId);
		response.put("orderName", "Mock 주문");
		response.put("requestedAt", "2024-11-18T12:00:00+09:00");
		response.put("approvedAt", "2024-11-18T12:01:00+09:00");
		response.put("useEscrow", false);
		response.put("cultureExpense", false);

		ObjectNode card = response.putObject("card");
		card.put("issuerCode", "61");
		card.put("acquirerCode", "31");
		card.put("number", "12345678****789*");
		card.put("installmentPlanMonths", 0);
		card.put("isInterestFree", false);
		card.put("approveNo", "00000000");
		card.put("cardType", "신용");
		card.put("ownerType", "개인");
		card.put("amount", amount);

		response.put("totalAmount", amount);
		response.put("balanceAmount", amount);
		response.put("currency", "KRW");

		return response;
	}

	// 결제 조회 (paymentKey) 시뮬레이션
	@Override
	public JsonNode getPaymentByPaymentKey(String paymentKey) {
		ObjectNode response = objectMapper.createObjectNode();
		response.put("paymentKey", paymentKey);
		response.put("orderId", "1");
		response.put("status", "DONE");
		response.put("amount", 15000);
		response.put("currency", "KRW");

		return response;
	}

	// 결제 조회 (orderId) 시뮬레이션
	@Override
	public JsonNode getPaymentByOrderId(String orderId) {
		ObjectNode response = objectMapper.createObjectNode();
		response.put("orderId", orderId);
		response.put("paymentKey", "mockPaymentKey123");
		response.put("status", "DONE");
		response.put("amount", 15000);
		response.put("currency", "KRW");

		return response;
	}

	// 결제 취소 시뮬레이션
	@Override
	public JsonNode cancelPayment(String paymentKey, String cancelReason) {
		ObjectNode response = objectMapper.createObjectNode();
		response.put("paymentKey", paymentKey);
		response.put("status", "CANCELLED");
		response.put("cancelReason", cancelReason);
		response.put("cancelledAt", "2024-11-18T13:00:00+09:00");

		return response;
	}
}