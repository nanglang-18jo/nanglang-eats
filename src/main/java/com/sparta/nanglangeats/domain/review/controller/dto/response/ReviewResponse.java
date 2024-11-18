package com.sparta.nanglangeats.domain.review.controller.dto.response;

import java.util.UUID;

import com.sparta.nanglangeats.domain.review.entity.Review;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewResponse {
	private UUID reviewId;
	private UUID userId;
	private UUID storeId;
	private UUID orderId;
	private String content;
	private int rating;




	public ReviewResponse(Review review){
		this.reviewId = review.getReviewId();
		this.userId = review.getUser().getUserId();
		this.storeId = review.getStore().getStoreId();
		this.orderId = review.getOrder().getOrderId();
		this.content = review.getContent();
		this.rating = review.getRating();

	}
}
