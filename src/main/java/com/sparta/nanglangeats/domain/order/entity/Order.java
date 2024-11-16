package com.sparta.nanglangeats.domain.order.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.nanglangeats.domain.order.enums.OrderStatus;
import com.sparta.nanglangeats.domain.order.enums.OrderType;
import com.sparta.nanglangeats.global.common.entity.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String storeId;

	@Column(nullable = false, unique = true)
	private String orderUuid;

	@Column(nullable = false)
	private Integer orderNumber;

	private String address;

	private String requirement;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderType type; // 주문 방식 [ONLINE, ONSITE]

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status; // 주문 상태 [PENDING, COMPLETE, CANCELED]

	@Column(nullable = false)
	private Long totalPrice;

	@Column(nullable = false)
	private boolean isActive;

	@Builder
	public Order(Long userId, String storeId, String orderUuid, Integer orderNumber, String address,
		String requirement, OrderType type, OrderStatus status, Long totalPrice) {
		this.userId = userId;
		this.storeId = storeId;
		this.orderUuid = orderUuid != null ? orderUuid : UUID.randomUUID().toString();
		this.orderNumber = orderNumber;
		this.address = address;
		this.requirement = requirement;
		this.type = type;
		this.status = status;
		this.totalPrice = totalPrice;
		this.isActive = true;
	}

	public void updateStatus(OrderStatus status) {
		this.status = status;
	}

	// Soft Delete
	public void delete(Long userId) {
		this.isActive = false;
		this.setDeletedAt(LocalDateTime.now());
		this.setDeletedBy(userId.toString());
	}
}