package com.sparta.nanglangeats.domain.product.controller;

import static com.sparta.nanglangeats.global.common.util.ControllerUtil.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.product.controller.dto.request.ProductRequest;
import com.sparta.nanglangeats.domain.product.service.ProductService;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProductController {

	private final ProductService productService;

	@PostMapping("/products")
	@PreAuthorize("hasAnyRole('OWNER')")
	public ResponseEntity<CommonResponse<?>> createProduct(
		@ModelAttribute @Valid ProductRequest request,
		@AuthenticationPrincipal User user){
		return getResponseEntity(HttpStatus.CREATED, productService.createProduct(request, user), "상품 등록 완료");
	}
	
	@PutMapping("/products/{productUuid}")
	@PreAuthorize("hasAnyRole('OWNER')")
	public ResponseEntity<CommonResponse<?>> updateProduct(
		@PathVariable String productUuid,
		@ModelAttribute @Valid ProductRequest request,
		@AuthenticationPrincipal User user){
		return getOkResponseEntity(productService.updateProduct(productUuid, request, user), "상품 수정 완료");
	}

	@DeleteMapping("/products/{productUuid}")
	@PreAuthorize("hasAnyRole('OWNER')")
	public ResponseEntity<CommonResponse<?>> deleteProduct(
		@PathVariable String productUuid,
		@AuthenticationPrincipal User user){
		productService.deleteProduct(productUuid, user);
		return getResponseEntity(HttpStatus.NO_CONTENT, null, "상품 삭제 완료");
	}

	@GetMapping("/products/{productUuid}")
	public ResponseEntity<CommonResponse<?>> getProductDetail(
		@PathVariable String productUuid){
		return getOkResponseEntity(productService.getProductDetail(productUuid), "상품 상세 조회 완료");
	}

	@GetMapping("/stores/{storeUuid}/products")
	public ResponseEntity<CommonResponse<?>> getProductsByStoresList(
		@PathVariable String storeUuid){
		return getOkResponseEntity(productService.getProductsByStoresList(storeUuid), "상품 목록 조회 완료");
	}

	@PatchMapping("/products/{productUuid}")
	@PreAuthorize("hasAnyRole('OWNER')")
	public ResponseEntity<CommonResponse<?>> updateProductVisibility(
		@PathVariable String productUuid,
		@AuthenticationPrincipal User user){
		return getOkResponseEntity(productService.updateProductVisibility(productUuid, user), "상품 공개/비공개 전환 완료");
	}
}
