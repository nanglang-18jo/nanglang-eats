package com.sparta.nanglangeats.domain.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.product.controller.dto.request.ProductCreateRequest;
import com.sparta.nanglangeats.domain.product.service.ProductService;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static com.sparta.nanglangeats.global.common.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	@PostMapping
	public ResponseEntity<CommonResponse<?>> createProduct(
		@ModelAttribute @Valid ProductCreateRequest request,
		@AuthenticationPrincipal User user){
		return getResponseEntity(productService.createProduct(request, user), "상품 등록 완료");
	}
}
