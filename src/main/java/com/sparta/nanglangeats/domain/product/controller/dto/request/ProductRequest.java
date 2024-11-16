package com.sparta.nanglangeats.domain.product.controller.dto.request;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductRequest {
	@NotNull(message = "가게 ID를 입력해주세요.")
	private String storeUuid;

	@NotBlank(message = "상품명을 입력해주세요.")
	private String name;

	private String description;

	@NotNull(message = "상품 가격을 입력해주세요.")
	private int price;

	private MultipartFile thumbnail;

	private List<MultipartFile> images;
}
