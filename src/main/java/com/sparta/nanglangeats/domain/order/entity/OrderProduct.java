package com.sparta.nanglangeats.domain.order.entity;

import com.sparta.nanglangeats.global.common.entity.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_order_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderProduct extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderProductId;

	@Column(nullable = false)
	private String orderId;

	@Column(nullable = false)
	private String productId;

	@Column(nullable = false)
	private Integer quantity;

	@Builder
	public OrderProduct(String orderId, String productId, Integer quantity) {
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
	}
}