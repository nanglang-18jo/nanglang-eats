package com.sparta.nanglangeats.domain.review.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.sparta.nanglangeats.domain.review.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MyReviewListResponse {

	private String storeName;
	private String reviewUuid;
	private String content;
	private int rating;
	private List<String> imageUrls;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static MyReviewListResponse of(Review review, List<String> imageUrls) {
		return new MyReviewListResponse(
			review.getStore().getName(),
			review.getUuid(),
			review.getContent(),
			review.getRating(),
			imageUrls,
			review.getCreatedAt(),
			review.getUpdatedAt());
	}
}
