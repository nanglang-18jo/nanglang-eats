package com.sparta.nanglangeats.domain.order.controller.dto.request;

import com.sparta.nanglangeats.domain.order.enums.OrderStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderStatusUpdateRequest {

	@NotNull(message = "주문 상태(status)는 필수 항목입니다.")
	private OrderStatus status;
}