package com.sparta.nanglangeats.domain.order.controller.dto.response;

import java.time.LocalDateTime;

import com.sparta.nanglangeats.domain.order.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderSummaryResponse {
	private Long orderId;
	private String status;
	private LocalDateTime createdAt;
	private Long totalPrice;

	public OrderSummaryResponse(Order order) {
		this.orderId = order.getOrderId();
		this.status = order.getStatus().name();
		this.createdAt = order.getCreatedAt();
		this.totalPrice = order.getTotalPrice();
	}
}