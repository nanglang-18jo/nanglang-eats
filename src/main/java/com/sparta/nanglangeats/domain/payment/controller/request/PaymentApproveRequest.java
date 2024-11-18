package com.sparta.nanglangeats.domain.payment.controller.request;

import com.sparta.nanglangeats.domain.payment.enums.PaymentType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaymentApproveRequest {

	@NotBlank(message = "결제 키는 필수 값입니다.")
	private String paymentKey;

	@NotNull(message = "주문 ID는 필수 값입니다.")
	private String orderId;

	@NotNull(message = "결제 금액은 필수 값입니다.")
	@Min(value = 1, message = "결제 금액은 0원 이상이어야 합니다.")
	private Long paymentAmount;

	@NotNull(message = "결제 방식은 필수 값입니다.")
	private PaymentType paymentType;
}
