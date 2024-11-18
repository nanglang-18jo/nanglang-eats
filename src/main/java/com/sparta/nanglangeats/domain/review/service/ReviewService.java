package com.sparta.nanglangeats.domain.review.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.image.enums.ImageCategory;
import com.sparta.nanglangeats.domain.image.service.ImageService;
import com.sparta.nanglangeats.domain.order.entity.Order;
import com.sparta.nanglangeats.domain.order.repository.OrderRepository;
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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final OrderRepository orderRepository;
	private final StoreRepository storeRepository;
	private final UserRepository userRepository;
	private final ImageService imageService;

	//리뷰 생성
	@Transactional
	public ReviewResponse createReview(ReviewRequest request, User user) {
		Order order = findOrderByUuid(request.getOrderUuid());

		checkDuplicateReview(order.getOrderId());

		Store store = findStoreByUuid(order.getStoreId());

		validateOrderOwner(order.getUserId(), user.getId());

		Review review = Review.builder()
			.order(order)
			.user(user)
			.store(store)
			.rating(request.getRating())
			.content(request.getContent())
			.build();

		reviewRepository.save(review);

		List<String> imagesUrl = null;
		if (!request.getImages().isEmpty()) {
			imagesUrl = imageService.uploadAllImages(request.getImages(), ImageCategory.REVIEW_IMAGE, review.getId());
		}

		store.calculateNewRating(request.getRating());
		return ReviewResponse.builder().review(review).imagesUrl(imagesUrl).build();
	}

	//리뷰 수정
	@Transactional
	public ReviewResponse updateReview(String reviewUuid, ReviewRequest request, User user) {
		Review review = findReviewByUuid(reviewUuid);

		validateReviewOwner(review.getUser().getId(), user.getId());

		review.update(request);

		imageService.hardDeleteAllImages(ImageCategory.REVIEW_IMAGE, review.getId());

		List<String> imagesUrl = null;
		if (request.getImages() != null) {
			imagesUrl = imageService.uploadAllImages(request.getImages(), ImageCategory.REVIEW_IMAGE, review.getId());
		}

		review.getStore().calculateEditRating(request.getRating());

		return ReviewResponse.builder().review(review).imagesUrl(imagesUrl).build();
	}

	//
	// // 가게 고유 ID로 리뷰 리스트 조회
	// @Transactional(readOnly = true)
	// public List<ReviewResponse> getReviewsByStoreId(UUID storeId) {
	// 	Store store = storeRepository.findById(storeId)
	// 		.orElseThrow(() -> new IllegalArgumentException("음식점 정보를 찾을 수 없습니다"));
	//
	// 	// 삭제되지 않은 리뷰만 조회
	// 	List<Review> reviews = reviewRepository.findByStoreAndDeletedFalse(store);
	//
	// 	return reviews.stream()
	// 		.map(ReviewResponse::new)
	// 		.collect(Collectors.toList());
	// }
	//
	// // 다른 사용자 리뷰 목록 조회
	// public List<ReviewResponse> getReviewsByUser(UUID userId) {
	// 	User user = userRepository.findById(userId)
	// 		.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
	// 	// 삭제되지 않은 리뷰만 조회
	// 	List<Review> reviews = reviewRepository.findByUserAndDeletedFalse(user);
	// 	return reviews.stream()
	// 		.map(ReviewResponse::new)
	// 		.collect(Collectors.toList());
	// }
	//

	// @Transactional
	// public void deleteReview(UUID reviewId, String deletedBy) {
	//
	// 	Review review = reviewRepository.findById(reviewId)
	// 		.orElseThrow(() -> new IllegalArgumentException("해당 리뷰는 삭제되었습니다 : " + reviewId));
	//
	// 	review.deleteReview(deletedBy);
	//
	// 	reviewRepository.save(review);  // 업데이트된 리뷰 저장
	// }

	/* UTIL */

	private void checkDuplicateReview(Long orderId) {
		if (reviewRepository.existsByOrderOrderId(orderId))
			throw new CustomException(ErrorCode.REVIEW_ALREADY_EXISTS);
	}

	private void validateOrderOwner(Long orderOwnerId, Long requestUserId) {
		if (!orderOwnerId.equals(requestUserId))
			throw new CustomException(ErrorCode.ACCESS_DENIED);
	}

	private void validateReviewOwner(Long reviewOwnerId, Long requestUserId) {
		if (!reviewOwnerId.equals(requestUserId))
			throw new CustomException(ErrorCode.ACCESS_DENIED);
	}

	private Review findReviewByUuid(String reviewUuid) {
		return reviewRepository.findByUuid(reviewUuid)
			.orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
	}

	private Order findOrderByUuid(String uuid) {
		return orderRepository.findByOrderUuid(uuid).orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
	}

	private Store findStoreByUuid(String storeUuid) {
		return storeRepository.findByUuid(storeUuid).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
	}
}
