package com.sparta.nanglangeats.domain.order.controller.dto.response;

import com.sparta.nanglangeats.domain.order.entity.OrderProduct;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderProductResponse {
	private Long orderProductId;
	private Integer quantity;
	private Long price;
	private String productId;

	public OrderProductResponse(OrderProduct orderProduct) {
		this.orderProductId = orderProduct.getOrderProductId();
		this.quantity = orderProduct.getQuantity();
		this.price = orderProduct.getPrice();
		this.productId = orderProduct.getProductId();
	}
}
