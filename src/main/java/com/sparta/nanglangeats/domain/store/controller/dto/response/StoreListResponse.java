package com.sparta.nanglangeats.domain.store.controller.dto.response;

import com.sparta.nanglangeats.domain.store.entity.Store;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreListResponse {
	private final String uuid;
	private final String name;
	private final Float rating;
	private final Integer reviewCount;
	private final String thumbnailUrl;

	@Builder
	public StoreListResponse(Store store){
		this.uuid = store.getUuid();
		this.name = store.getName();
		this.rating = store.getRating();
		this.reviewCount = store.getReviewCount();
		this.thumbnailUrl = store.getThumbnailUrl();
	}
}
