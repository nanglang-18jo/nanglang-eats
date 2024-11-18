package com.sparta.nanglangeats.domain.review.controller.dto;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.review.controller.dto.request.ReviewRequest;
import com.sparta.nanglangeats.domain.review.service.ReviewService;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;

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

	// 가게별 리뷰 목록 조회
	@GetMapping("/{storeUuid}")
	public ResponseEntity<CommonResponse<?>> getReviewsList(
		@PathVariable String storeUuid,
		@RequestParam(defaultValue = "0") int page) {
		return getOkResponseEntity(reviewService.getReviewList(storeUuid, page), "가게별 리뷰 목록 조회");
	}
}
