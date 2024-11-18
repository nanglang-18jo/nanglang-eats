package com.sparta.nanglangeats.domain.store.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreResponse {
	private final String storeUuid;

	@Builder
	public StoreResponse(String storeUuid) {
		this.storeUuid = storeUuid;
	}
}
