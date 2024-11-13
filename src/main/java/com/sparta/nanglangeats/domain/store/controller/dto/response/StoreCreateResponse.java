package com.sparta.nanglangeats.domain.store.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreCreateResponse {
	private final String storeId;

	@Builder
	public StoreCreateResponse(String storeId) {
		this.storeId = storeId;
	}
}
