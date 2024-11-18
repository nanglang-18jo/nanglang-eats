package com.sparta.nanglangeats.domain.review.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.nanglangeats.domain.order.entity.Order;
import com.sparta.nanglangeats.domain.store.entity.Store;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.entity.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;

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
	private float rating;

	@Column(nullable = false)
	private String content;

	@Column(nullable = false)
	private String image;

	@Column(nullable = false)
	private Boolean isActive;

	@Builder
	public Review(Order order, User user, Store store, String content, String image, float rating) {
		this.uuid = UUID.randomUUID().toString(); // uuid 자동 생성
		this.order = order;
		this.user = user;
		this.store = store;
		this.content = content;
		this.image = image;
		setRating(rating);
		this.isActive = true;
	}

	public void setRating(float rating) {
		if (rating < 1 || rating > 5) {
			throw new IllegalArgumentException("평점은 1.0점에서 5.0점 사이입니다.");
		}
		this.rating = rating;
	}

	public void updateReview(String content, String image, float rating) {
		this.content = content;
		this.image = image;
		this.rating = rating;
	}

	public void deleteReview(String deleteBy){
		this.isActive = false;
		this.setDeletedAt(LocalDateTime.now());
		this.setDeletedBy(deleteBy);
	}
}
