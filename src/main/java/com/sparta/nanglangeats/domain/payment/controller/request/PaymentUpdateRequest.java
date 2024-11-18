package com.sparta.nanglangeats.domain.payment.controller.request;

import com.sparta.nanglangeats.domain.payment.enums.PaymentType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentUpdateRequest {
	private String amount;
	private String status;
	private PaymentType type;
}