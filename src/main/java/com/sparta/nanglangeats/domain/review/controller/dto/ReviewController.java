package com.sparta.nanglangeats.domain.review.controller.dto;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.review.controller.dto.request.ReviewRequest;
import com.sparta.nanglangeats.domain.review.controller.dto.response.ReviewResponse;
import com.sparta.nanglangeats.domain.review.service.ReviewService;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.service.UserService;
import com.sparta.nanglangeats.global.config.security.provider.CustomAuthenticationProvider;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;
	private final UserService userService;

	// 리뷰 생성
	@PostMapping
	public ResponseEntity<ReviewResponse> createReview(@RequestBody ReviewRequest request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		ReviewResponse responseDto = reviewService.createReview(request, userDetails.getUser());
		return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
	}

	// 음식점 ID로 해당 음식점의 리뷰 리스트 조회
	@GetMapping("/store/{storeId}")
	public ResponseEntity<List<ReviewResponse>> getReviewsByStoreId(@PathVariable UUID storeId) {
		List<ReviewResponse> reviews = reviewService.getReviewsByStoreId(storeId);
		return ResponseEntity.status(HttpStatus.OK).body(reviews);
	}

	// 특정 사용자에 대한 리뷰 조회
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<ReviewResponse>> getReviewsByUser(@PathVariable UUID userId) {
		List<ReviewResponse> reviews = reviewService.getReviewsByUser(userId);
		return ResponseEntity.ok(reviews);
	}

	// 특정 주문에 대한 리뷰 조회
	@GetMapping("/order/{orderId}")
	public ResponseEntity<ReviewResponse> getReviewByOrder(@PathVariable UUID orderId) {
		return ResponseEntity.ok(reviewService.getReviewByOrder(orderId));
	}

	// 리뷰 수정
	@PutMapping("/{reviewId}")
	public ResponseEntity<ReviewResponse> updateReview(
		@PathVariable UUID reviewId,
		@RequestBody ReviewRequest requestDto,
		@AuthenticationPrincipal CustomAuthenticationProvider customAuthenticationProvider) {
		ReviewResponse responseDto = reviewService.updateReview(reviewId, requestDto, customAuthenticationProvider.getUser());
		return ResponseEntity.ok(responseDto);
	}

	@DeleteMapping("/{reviewId}")
	public ResponseEntity<String> deleteReview(@PathVariable UUID reviewId, @AuthenticationPrincipal User user) {
		// 인증된 사용자 확인
		if (user == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized: User not logged in");
		}

		// 삭제 요청 처리
		String deletedBy = user.getUser(); // 삭제한 사용자 정보
		reviewService.deleteReview(reviewId, deletedBy);

		return ResponseEntity.status(HttpStatus.OK).body("리뷰 삭제 요청 처리가 완료되었습니다.");
	}
}
