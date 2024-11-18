package com.sparta.nanglangeats.domain.payment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nanglangeats.domain.payment.client.TossPaymentClient;

class TossPaymentClientTest {

	private TossPaymentClient tossPaymentClient;
	private RestTemplate restTemplate;

	@BeforeEach
	void setUp() {
		// Mock RestTemplate 주입
		restTemplate = Mockito.mock(RestTemplate.class);
		tossPaymentClient = new TossPaymentClient(restTemplate);
	}

	@Test
	void getPaymentByOrderId_Success() throws Exception {
		// Given
		String orderId = "1";

		String mockResponse = """
				{
				  "mId": "tvivarepublica2",
				  "lastTransactionKey": "2709F1D8C5B5B741A7DC04D476622629",
				  "paymentKey": "qKl56WYb7w4vZnjEJeQVxzdjlXqKM8PmOoBN0k12dzgRG9px",
				  "orderId": "1",
				  "orderName": "test",
				  "taxExemptionAmount": 0,
				  "status": "DONE",
				  "requestedAt": "2022-12-06T12:53:22+09:00",
				  "approvedAt": "2022-12-06T12:53:22+09:00",
				  "useEscrow": false,
				  "cultureExpense": false,
				  "card": {
					"issuerCode": "11",
					"acquirerCode": "11",
					"number": "55704203****001*",
					"installmentPlanMonths": 0,
					"isInterestFree": false,
					"interestPayer": null,
					"approveNo": "00000000",
					"useCardPoint": false,
					"cardType": "신용",
					"ownerType": "개인",
					"acquireStatus": "READY",
					"amount": 100
				  },
				  "virtualAccount": null,
				  "transfer": null,
				  "mobilePhone": null,
				  "giftCertificate": null,
				  "cashReceipt": null,
				  "cashReceipts": null,
				  "discount": null,
				  "cancels": null,
				  "secret": "ps_Kma60RZblrqREqw69Ob8wzYWBn14",
				  "type": "BILLING",
				  "easyPay": null,
				  "country": "KR",
				  "failure": null,
				  "isPartialCancelable": true,
				  "receipt": {
					"url": "https://dashboard.tosspayments.com/receipt/redirection?transactionId=tviva20221206125322PGcz7&ref=PX"
				  },
				  "checkout": {
					"url": "https://api.tosspayments.com/v1/payments/qKl56WYb7w4vZnjEJeQVxzdjlXqKM8PmOoBN0k12dzgRG9px/checkout"
				  },
				  "currency": "KRW",
				  "totalAmount": 100,
				  "balanceAmount": 100,
				  "suppliedAmount": 91,
				  "vat": 9,
				  "taxFreeAmount": 0,
				  "method": "카드",
				  "version": "2022-11-16",
				  "metadata": null
				}
			""";

		// Mock 응답 데이터를 JsonNode로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode mockResponseJson = objectMapper.readTree(mockResponse);

		String url = "https://api.tosspayments.com/v1/payments/orders/" + orderId;
		ResponseEntity<JsonNode> mockResponseEntity = ResponseEntity.ok(mockResponseJson);

		// RestTemplate의 getForEntity 호출을 Mock 처리
		when(restTemplate.getForEntity(eq(url), eq(JsonNode.class), any(HttpEntity.class)))
			.thenReturn(mockResponseEntity);

		// When
		JsonNode response = tossPaymentClient.getPaymentByOrderId(orderId);

		// Then
		assertNotNull(response);
		assertEquals("1", response.get("orderId").asText());
		assertEquals("qKl56WYb7w4vZnjEJeQVxzdjlXqKM8PmOoBN0k12dzgRG9px", response.get("paymentKey").asText());
		assertEquals("DONE", response.get("status").asText());

		// RestTemplate 호출 확인
		verify(restTemplate, times(1)).getForEntity(eq(url), eq(JsonNode.class), any(HttpEntity.class));
	}

	@Test
	void getPaymentByOrderId_Failure() {
		// Given: 존재하지 않는 Order ID
		String orderId = "999"; // 잘못된 Order ID
		String url = "https://api.tosspayments.com/v1/payments/orders/" + orderId;

		// Toss API에서 반환하는 실제 응답을 설정
		String errorResponse = """
			    {
			      "code": "NOT_FOUND_PAYMENT",
			      "message": "존재하지 않는 결제 정보 입니다."
			    }
			""";

		// RestTemplate 호출 시 던질 예외 설정
		HttpClientErrorException mockException = new HttpClientErrorException(
			HttpStatus.NOT_FOUND, "Not Found",
			errorResponse.getBytes(), null
		);

		when(restTemplate.getForEntity(eq(url), eq(JsonNode.class), any(HttpEntity.class)))
			.thenThrow(mockException);

		// When & Then: 예외를 발생시키고 메시지 검증
		HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> {
			tossPaymentClient.getPaymentByOrderId(orderId);
		});

		// 예외 메시지 검증
		assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode()); // HTTP 상태 코드 확인
		assertTrue(exception.getResponseBodyAsString().contains("NOT_FOUND_PAYMENT")); // 응답 코드 확인
		assertTrue(exception.getResponseBodyAsString().contains("존재하지 않는 결제 정보 입니다.")); // 응답 메시지 확인

		// RestTemplate 호출 횟수 검증
		verify(restTemplate, times(1)).getForEntity(eq(url), eq(JsonNode.class), any(HttpEntity.class));
	}
}
