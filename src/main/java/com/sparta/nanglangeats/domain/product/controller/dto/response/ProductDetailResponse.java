package com.sparta.nanglangeats.domain.product.controller.dto.response;

import java.util.List;

import com.sparta.nanglangeats.domain.product.entity.Product;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ProductDetailResponse {
	private final String name;
	private final String description;
	private final int price;
	private final List<String> imageUrls;

	@Builder
	public ProductDetailResponse(Product product, List<String> imageUrls) {
		this.name = product.getName();
		this.description = product.getDescription();
		this.price = product.getPrice();
		this.imageUrls = imageUrls;
	}
}
