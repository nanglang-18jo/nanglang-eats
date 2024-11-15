package com.sparta.nanglangeats.domain.product.controller;

import static com.sparta.nanglangeats.global.common.util.ControllerUtil.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/api/products")
public class ProductController {

	private final ProductService productService;

	@PostMapping
	@PreAuthorize("hasAnyRole('OWNER')")
	public ResponseEntity<CommonResponse<?>> createProduct(
		@ModelAttribute @Valid ProductRequest request,
		@AuthenticationPrincipal User user){
		return getResponseEntity(HttpStatus.CREATED, productService.createProduct(request, user), "상품 등록 완료");
	}
	
	@PutMapping("/{uuid}")
	@PreAuthorize("hasAnyRole('OWNER')")
	public ResponseEntity<CommonResponse<?>> updateProduct(
		@PathVariable String uuid,
		@ModelAttribute @Valid ProductRequest request,
		@AuthenticationPrincipal User user){
		return getOkResponseEntity(productService.updateProduct(uuid, request, user), "상품 수정 완료");
	}

	@DeleteMapping("/{uuid}")
	@PreAuthorize("hasAnyRole('OWNER')")
	public ResponseEntity<CommonResponse<?>> deleteProduct(
		@PathVariable String uuid,
		@AuthenticationPrincipal User user){
		productService.deleteProduct(uuid, user);
		return getResponseEntity(HttpStatus.NO_CONTENT, null, "상품 삭제 완료");
	}

	@GetMapping("/{uuid}")
	public ResponseEntity<CommonResponse<?>> getProductDetail(
		@PathVariable String uuid){
		return getOkResponseEntity(productService.getProductDetail(uuid), "상품 상세 조회 완료");
	}
}
