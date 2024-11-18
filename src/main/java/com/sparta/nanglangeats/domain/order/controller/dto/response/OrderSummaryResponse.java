package com.sparta.nanglangeats.domain.order.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.sparta.nanglangeats.domain.order.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderSummaryResponse {
	private Long orderId;
	private String orderUuid;
	private String status;
	private LocalDateTime createdAt;
	private Long totalPrice;
	private String storeName;
	private List<OrderProductResponse> products;

	public OrderSummaryResponse(Order order, String storeName) {
		this.orderId = order.getOrderId();
		this.orderUuid = order.getOrderUuid();
		this.status = order.getStatus().name();
		this.createdAt = order.getCreatedAt();
		this.totalPrice = order.getTotalPrice();
		this.storeName = storeName;
		this.products = order.getOrderProducts().stream()
			.map(OrderProductResponse::new)
			.collect(Collectors.toList());
	}
}