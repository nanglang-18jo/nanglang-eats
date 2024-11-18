package com.sparta.nanglangeats.domain.review.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.order.entity.Order;
import com.sparta.nanglangeats.domain.review.controller.dto.request.ReviewRequest;
import com.sparta.nanglangeats.domain.review.controller.dto.response.ReviewResponse;
import com.sparta.nanglangeats.domain.review.entity.Review;
import com.sparta.nanglangeats.domain.review.repository.ReviewRepository;
import com.sparta.nanglangeats.domain.store.entity.Category;
import com.sparta.nanglangeats.domain.store.entity.Store;
import com.sparta.nanglangeats.domain.store.repository.StoreRepository;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.domain.user.repository.UserRepository;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.common.exception.ErrorCode;

public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final OrderRepository orderRepository;
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;

	//리뷰 생성
	@Transactional
	public ReviewResponse createReview(ReviewRequest request, User user) {
		Order order = orderRepository.findById(request.getUserId())
			.orElseThrow(() -> new IllegalArgumentException("Invalid User ID"));
		if(!order.getUser().equals(user)){
			throw new IllegalArgumentException("이 주문은 해당 사용자에게 속하지 않습니다.");
		}
		Store store = storeRepository.findById(request.getReviewId())
			.orElseThrow(() -> new IllegalArgumentException("Invalid Store ID"));

		Review review = Review.builder()
			.user(user)
			.store(store)
			.order(order)
			.content(request.getContent())
			.rating(request.getRating())
			.build();

		reviewRepository.save(review);

		return new ReviewResponse(review);
	}

	// 음식점 고유 ID로 리뷰 리스트 조회
	@Transactional(readOnly = true)
	public List<ReviewResponse> getReviewsByStoreId(UUID storeId) {
		Store store = storeRepository.findById(storeId)
			.orElseThrow(() -> new IllegalArgumentException("음식점 정보를 찾을 수 없습니다"));

		// 삭제되지 않은 리뷰만 조회
		List<Review> reviews = reviewRepository.findByStoreAndDeletedFalse(store);

		return reviews.stream()
			.map(ReviewResponse::new)
			.collect(Collectors.toList());
	}

	// 특정 사용자 대한 리뷰 조회
	public List<ReviewResponse> getReviewsByUser(UUID userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
		// 삭제되지 않은 리뷰만 조회
		List<Review> reviews = reviewRepository.findByUserAndDeletedFalse(user);
		return reviews.stream()
			.map(ReviewResponse::new)
			.collect(Collectors.toList());
	}

	// 특정 주문 대한 리뷰 조회
	public ReviewResponse getReviewByOrder(UUID orderId) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException("주문 정보를 찾을 수 없습니다"));

		// 삭제되지 않은 리뷰만 조회
		Review review = reviewRepository.findByOrderAndDeletedFalse(order);
		return new ReviewResponse(review);
	}

	//리뷰 수정
	@Transactional
	public ReviewResponse updateReview(UUID reviewId, ReviewRequest request, User user) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new IllegalArgumentException("Review not found with : " + reviewId));

		if(!review.getUser().equals(user)){
			throw new SecurityException("해당 리뷰는 수정할 수가 없습니다.");
		}
		review.updateReview(request.getContent(), request.getRating());
		return new ReviewResponse(review);
	}

	@Transactional
	public void deleteReview(UUID reviewId, String deletedBy) {

		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new IllegalArgumentException("해당 리뷰는 삭제되었습니다 : " + reviewId));

		review.deleteReview(deletedBy);

		reviewRepository.save(review);  // 업데이트된 리뷰 저장
	}

	/* UTIL */
	private User validateOwner(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		if (!user.getRole().equals(UserRole.OWNER))
			throw new CustomException(ErrorCode.USER_ROLE_NOT_OWNER);
		return user;
	}

	private Review findReviewByUuid(String uuid) {
		return reviewRepository.findByUuid(uuid).orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
	}
}
