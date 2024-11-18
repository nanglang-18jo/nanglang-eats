package com.sparta.nanglangeats.domain.review.controller.dto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

	// 리뷰 수정
	@PatchMapping("/{reviewUuid}")
	@PreAuthorize("hasRole('CUSTOMER')")
	public ResponseEntity<CommonResponse<?>> updateReview(
		@PathVariable String reviewUuid,
		@ModelAttribute @Valid ReviewRequest request,
		@AuthenticationPrincipal User user) {
		return getOkResponseEntity(reviewService.updateReview(reviewUuid, request, user), "리뷰 수정 완료");
	}

	// 리뷰 삭제
	@DeleteMapping("/{reviewUuid}")
	@PreAuthorize("hasAnyRole('CUSTOMER','MASTER','MANAGER')")
	public ResponseEntity<CommonResponse<?>> deleteReview(
		@PathVariable String reviewUuid,
		@AuthenticationPrincipal User user) {
		reviewService.deleteReview(reviewUuid, user);
		return getResponseEntity(HttpStatus.NO_CONTENT, null, "리뷰 삭제 완료");
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
	

	// 	// 삭제 요청 처리
	// 	String deletedBy = user.getUser(); // 삭제한 사용자 정보
	// 	reviewService.deleteReview(reviewId, deletedBy);
	//
	// 	return ResponseEntity.status(HttpStatus.OK).body("리뷰 삭제 요청 처리 완료");
	// }
}
