package com.sparta.nanglangeats.domain.product.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductCreateResponse {
	private final String productId;

	@Builder
	public ProductCreateResponse(String productId) {
		this.productId = productId;
	}
}
