package com.sparta.nanglangeats.domain.review.controller.dto.response;

import java.util.List;
import java.util.UUID;

import com.sparta.nanglangeats.domain.review.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ReviewResponse {
	private final String reviewUuid;
	private final String content;
	private final int rating;
	private final List<String> imagesUrl;

	@Builder
	public ReviewResponse(Review review, List<String> imagesUrl) {
		this.reviewUuid = review.getUuid();
		this.content = review.getContent();
		this.rating = review.getRating();
		this.imagesUrl = imagesUrl;
	}

}
