package com.sparta.nanglangeats.domain.product.controller.dto.response;

import com.sparta.nanglangeats.domain.product.entity.Product;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductListResponse {
	private final String uuid;
	private final String name;
	private final String description;
	private final int price;
	private final String thumbnailUrl;

	@Builder
	public ProductListResponse(Product product) {
		this.uuid = product.getUuid();
		this.name = product.getName();
		this.description = product.getDescription();
		this.price = product.getPrice();
		this.thumbnailUrl = product.getThumbnailUrl();
	}
}
