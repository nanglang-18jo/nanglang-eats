package com.sparta.nanglangeats.domain.store.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreResponse {
	private final String storeId;

	@Builder
	public StoreResponse(String storeId) {
		this.storeId = storeId;
	}
}
