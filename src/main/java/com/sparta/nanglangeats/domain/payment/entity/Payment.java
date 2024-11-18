package com.sparta.nanglangeats.domain.payment.entity;

import java.util.UUID;

import com.sparta.nanglangeats.domain.order.entity.Order;
import com.sparta.nanglangeats.domain.payment.enums.PaymentStatus;
import com.sparta.nanglangeats.domain.payment.enums.PaymentType;
import com.sparta.nanglangeats.global.common.entity.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "p_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = true)
	private Order order;

	@Column(nullable = false, unique = true)
	private String uuid;

	@Column(nullable = false, length = 200)
	private String paymentKey;

	@Column(nullable = false)
	private String amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentType type;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PaymentStatus status;

	@Builder
	public Payment(Order order, String paymentKey, String amount, PaymentType type, PaymentStatus status) {
		this.order = order;
		this.uuid = uuid != null ? uuid : UUID.randomUUID().toString(); // 자동 생성되는 UUID
		this.paymentKey = paymentKey;
		this.amount = amount;
		this.type = type;
		this.status = status;
	}

	// 결제 상태 업데이트
	public void updateStatus(PaymentStatus status) {
		this.status = status;
	}
}