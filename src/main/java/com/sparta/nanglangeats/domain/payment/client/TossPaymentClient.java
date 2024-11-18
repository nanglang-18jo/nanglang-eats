package com.sparta.nanglangeats.domain.payment.client;

import java.util.Base64;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class TossPaymentClient implements TossPayment {

	private static final String TOSS_BASE_URL = "https://api.tosspayments.com/v1";
	private static final String TOSS_SECRET_KEY = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

	private final RestTemplate restTemplate;

	public TossPaymentClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	// 결제 승인
	public JsonNode approvePayment(String paymentKey, String orderId, Long amount) {
		String url = TOSS_BASE_URL + "/payments/confirm";

		HttpHeaders headers = getAuthHeaders();
		headers.add("Content-Type", "application/json");

		String requestBody = String.format(
			"{\"paymentKey\":\"%s\", \"orderId\":\"%s\", \"amount\":%d}",
			paymentKey, orderId, amount
		);

		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

		try {
			ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);
			return response.getBody();
		} catch (HttpClientErrorException ex) {
			// Toss API에서 반환된 에러 그대로 전달
			throw ex;
		} catch (Exception ex) {
			// 기타 예외는 RuntimeException으로 래핑
			throw new RuntimeException("결제 승인 실패: " + ex.getMessage(), ex);
		}
	}

	// 결제 조회 (paymentKey)
	public JsonNode getPaymentByPaymentKey(String paymentKey) {
		String url = TOSS_BASE_URL + "/payments/" + paymentKey;

		HttpHeaders headers = getAuthHeaders();
		HttpEntity<Void> request = new HttpEntity<>(headers);

		try {
			ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class, request);
			return response.getBody();
		} catch (HttpClientErrorException ex) {
			// Toss API에서 반환된 에러 그대로 전달
			throw ex;
		} catch (Exception ex) {
			// 기타 예외는 RuntimeException으로 래핑
			throw new RuntimeException("결제를 찾을 수 없음: " + ex.getMessage(), ex);
		}
	}

	// 결제 조회 (orderId)
	public JsonNode getPaymentByOrderId(String orderId) {
		String url = TOSS_BASE_URL + "/payments/orders/" + orderId;

		HttpHeaders headers = getAuthHeaders();
		HttpEntity<Void> request = new HttpEntity<>(headers);

		try {
			ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class, request);
			return response.getBody();
		} catch (HttpClientErrorException ex) {
			// Toss API에서 반환된 에러 그대로 전달
			throw ex;
		} catch (Exception ex) {
			// 기타 예외는 RuntimeException으로 래핑
			throw new RuntimeException("결제를 찾을 수 없음: " + ex.getMessage(), ex);
		}
	}

	// 결제 취소
	public JsonNode cancelPayment(String paymentKey, String cancelReason) {
		String url = TOSS_BASE_URL + "/payments/" + paymentKey + "/cancel";

		HttpHeaders headers = getAuthHeaders();
		headers.add("Content-Type", "application/json");

		String requestBody = String.format("{\"cancelReason\":\"%s\"}", cancelReason);

		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

		try {
			ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);
			return response.getBody();
		} catch (HttpClientErrorException ex) {
			// Toss API에서 반환된 에러 그대로 전달
			throw ex;
		} catch (Exception ex) {
			// 기타 예외는 RuntimeException으로 래핑
			throw new RuntimeException("결제 취소 실패: " + ex.getMessage(), ex);
		}
	}

	// 인증 헤더 생성
	private HttpHeaders getAuthHeaders() {
		HttpHeaders headers = new HttpHeaders();
		String auth = "Basic " + Base64.getEncoder().encodeToString((TOSS_SECRET_KEY + ":").getBytes());
		headers.add("Authorization", auth);
		return headers;
	}
}