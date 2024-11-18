package com.sparta.nanglangeats.domain.review.controller.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sparta.nanglangeats.global.common.util.ControllerUtil.*;

import com.sparta.nanglangeats.domain.review.controller.dto.request.ReviewRequest;
import com.sparta.nanglangeats.domain.review.controller.dto.response.ReviewResponse;
import com.sparta.nanglangeats.domain.review.service.ReviewService;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;
import com.sparta.nanglangeats.global.config.security.provider.CustomAuthenticationProvider;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	// 리뷰 생성
	@PostMapping
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<CommonResponse<?>> createReview(
		@ModelAttribute @Valid ReviewRequest request,
		@AuthenticationPrincipal User user) {
		return getResponseEntity(HttpStatus.CREATED, reviewService.createReview(request, user), "리뷰 등록 완료");
	}
	//
	// // 내 리뷰 목록 조회
	// @GetMapping("/me")
	// @PreAuthorize("hasAnyRole('CUSTOMER')")
	// public ResponseEntity<ReviewResponse> getReviewByOrder(@PathVariable UUID orderId) {
	// 	return ResponseEntity.ok(reviewService.getReviewByOrder(orderId));
	// }
	//
	// // 특정 사용자에 대한 리뷰 조회
	// @GetMapping("/user/{userId}")
	// public ResponseEntity<List<ReviewResponse>> getReviewsByUser(@PathVariable UUID userId) {
	// 	List<ReviewResponse> reviews = reviewService.getReviewsByUser(userId);
	// 	return ResponseEntity.ok(reviews);
	// }
	//
	// // 음식점 ID로 해당 음식점의 리뷰 리스트 조회
	// @GetMapping("/{storeId}")
	// public ResponseEntity<List<ReviewResponse>> getReviewsByStoreId(@PathVariable UUID storeId) {
	// 	List<ReviewResponse> reviews = reviewService.getReviewsByStoreId(storeId);
	// 	return ResponseEntity.status(HttpStatus.OK).body(reviews);
	// }
	//
	// // 리뷰 수정
	// @PutMapping("/{reviewId}")
	// @PreAuthorize("hasAnyRole('CUSTOMER')")
	// public ResponseEntity<ReviewResponse> updateReview(
	// 	@PathVariable UUID reviewId,
	// 	@RequestBody ReviewRequest request,
	// 	@AuthenticationPrincipal CustomAuthenticationProvider customAuthenticationProvider) {
	// 	ReviewResponse responseDto = reviewService.updateReview(reviewId, request, customAuthenticationProvider.getUser());
	// 	return ResponseEntity.ok(responseDto);
	// }
	//
	// // 리뷰 삭제
	// @DeleteMapping("/{reviewId}")
	// @PreAuthorize("hasAnyRole('CUSTOMER','MASTER')")
	// public ResponseEntity<String> deleteReview(@PathVariable UUID reviewId, @AuthenticationPrincipal User user) {
	// 	// 인증된 사용자 확인
	// 	if (user == null) {
	// 		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: User not logged in");
	// 	}
	//
	// 	// 삭제 요청 처리
	// 	String deletedBy = user.getUser(); // 삭제한 사용자 정보
	// 	reviewService.deleteReview(reviewId, deletedBy);
	//
	// 	return ResponseEntity.status(HttpStatus.OK).body("리뷰 삭제 요청 처리 완료");
	// }
}
