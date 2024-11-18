package com.sparta.nanglangeats.domain.review.controller.dto.response;

import java.util.List;

import com.sparta.nanglangeats.domain.review.entity.Review;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReviewListResponse {
	private final String uuid;
	private final String nickname;
	private final int rating;
	private final String content;
	private final List<String> imageUrls;
	private final String createdAt;

	@Builder
	public ReviewListResponse(Review review, List<String> imageUrls){
		this.uuid = review.getUuid();
		this.nickname = review.getUser().getNickname();
		this.rating = review.getRating();
		this.content = review.getContent();
		this.imageUrls = imageUrls;
		this.createdAt = review.getCreatedAt().toString();
	}
}
