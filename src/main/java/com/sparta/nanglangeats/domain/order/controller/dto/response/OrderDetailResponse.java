package com.sparta.nanglangeats.domain.order.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.sparta.nanglangeats.domain.order.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderDetailResponse {
	private Long orderId;
	private String storeId;
	private Long userId;
	private String status;
	private LocalDateTime createdAt;
	private String storeName;
	private List<OrderProductResponse> products;

	public OrderDetailResponse(Order order, String storeName) {
		this.orderId = order.getOrderId();
		this.storeId = order.getStoreId();
		this.userId = order.getUserId();
		this.status = order.getStatus().name();
		this.createdAt = order.getCreatedAt();
		this.storeName = storeName; // 가게 이름 추가
		this.products = order.getOrderProducts().stream()
			.map(OrderProductResponse::new)
			.collect(Collectors.toList());
	}
}