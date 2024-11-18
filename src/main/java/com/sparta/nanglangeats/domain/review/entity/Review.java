package com.sparta.nanglangeats.domain.review.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.nanglangeats.domain.order.entity.Order;
import com.sparta.nanglangeats.domain.review.controller.dto.request.ReviewRequest;
import com.sparta.nanglangeats.domain.store.entity.Store;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.entity.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "p_review")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String uuid; // 노출되는 식별자

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@Column
	private int rating;

	private String content;

	@Column(nullable = false)
	private Boolean isActive;

	@Builder
	public Review(Order order, User user, Store store, int rating, String content) {
		this.uuid = UUID.randomUUID().toString();
		this.order = order;
		this.user = user;
		this.store = store;
		this.rating = rating;
		this.content = content;
		this.isActive = true;
	}

	public void update(ReviewRequest request) {
		this.content = request.getContent();
		this.rating = request.getRating();
	}

	// public void deleteReview(String deleteBy){
	// 	this.isActive = false;
	// 	this.setDeletedAt(LocalDateTime.now());
	// 	this.setDeletedBy(deleteBy);
	// }
}
