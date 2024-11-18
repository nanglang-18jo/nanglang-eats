package com.sparta.nanglangeats.domain.product.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductResponse {
	private final String productUuid;

	@Builder
	public ProductResponse(String productUuid) {
		this.productUuid = productUuid;
	}
}
