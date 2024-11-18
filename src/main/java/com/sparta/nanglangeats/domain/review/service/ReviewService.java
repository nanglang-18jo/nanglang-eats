package com.sparta.nanglangeats.domain.review.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.image.enums.ImageCategory;
import com.sparta.nanglangeats.domain.image.repository.ImageRepository;
import com.sparta.nanglangeats.domain.image.service.ImageService;
import com.sparta.nanglangeats.domain.order.entity.Order;
import com.sparta.nanglangeats.domain.order.repository.OrderRepository;
import com.sparta.nanglangeats.domain.review.controller.dto.request.ReviewRequest;
import com.sparta.nanglangeats.domain.review.controller.dto.response.MyReviewListResponse;
import com.sparta.nanglangeats.domain.review.controller.dto.response.ReviewListResponse;
import com.sparta.nanglangeats.domain.review.controller.dto.response.ReviewResponse;
import com.sparta.nanglangeats.domain.review.entity.Review;
import com.sparta.nanglangeats.domain.review.repository.ReviewRepository;
import com.sparta.nanglangeats.domain.store.entity.Store;
import com.sparta.nanglangeats.domain.store.repository.StoreRepository;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

	private static final int PAGE_SIZE = 10;

	private final ReviewRepository reviewRepository;
	private final OrderRepository orderRepository;
	private final StoreRepository storeRepository;
	private final ImageService imageService;
	private final ImageRepository imageRepository;

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

	@Transactional(readOnly = true)
	public List<MyReviewListResponse> getMyReviewList(User user, int page, int size, String sortBy) {
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sortBy));
		List<Review> reviews = reviewRepository.findAllMyReviews(user, pageRequest);
		return reviews.stream()
			.map(review -> {
				List<String> imageUrls = imageRepository.findUrlsByImageCategoryAndContentId(ImageCategory.REVIEW_IMAGE, review.getId());
				return MyReviewListResponse.of(review, imageUrls);
			}).toList();
	}

	@Transactional
	public ReviewResponse updateReview(String reviewUuid, ReviewRequest request, User user) {
		Review review = findReviewByUuid(reviewUuid);

		validateReviewOwner(review.getUser().getId(), user.getId());

		review.getStore().calculateUpdateRating(review.getRating(), request.getRating());
		review.update(request);

		imageService.hardDeleteAllImages(ImageCategory.REVIEW_IMAGE, review.getId());

		List<String> imagesUrl = null;
		if (request.getImages() != null) {
			imagesUrl = imageService.uploadAllImages(request.getImages(), ImageCategory.REVIEW_IMAGE, review.getId());
		}


		return ReviewResponse.builder().review(review).imagesUrl(imagesUrl).build();
	}

	@Transactional
	public void deleteReview(String reviewUuid, User user) {
		Review review = findReviewByUuid(reviewUuid);

		if (user.getRole().equals(UserRole.CUSTOMER))
			validateReviewOwner(review.getUser().getId(), user.getId());

		review.getStore().calculateDeleteRating(review.getRating());

		review.delete(user.getUsername());

		imageService.softDeleteAllImages(ImageCategory.REVIEW_IMAGE, review.getId(), user.getUsername());
	}

	public Page<ReviewListResponse> getReviewList(String storeUuid, int page){
		Store store = findStoreByUuid(storeUuid);

		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

		Page<Review> reviews = reviewRepository.findAllByStoreIdAndIsActiveTrueOrderByCreatedAtDesc(store.getId(), pageable);

		return reviews.map(review -> {
			List<String> imagesUrl = imageRepository.findUrlsByImageCategoryAndContentId(ImageCategory.REVIEW_IMAGE, review.getId());
			return ReviewListResponse.builder()
				.review(review)
				.imageUrls(imagesUrl)
				.build();
		});
	}

	/* UTIL */
	private void checkDuplicateReview(Long orderId) {
		if (reviewRepository.existsByOrderOrderIdAndIsActiveTrue(orderId))
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
		return reviewRepository.findByUuidAndIsActiveTrue(reviewUuid)
			.orElseThrow(() -> new CustomException(ErrorCode.REVIEW_NOT_FOUND));
	}

	private Order findOrderByUuid(String uuid) {
		return orderRepository.findByOrderUuid(uuid).orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
	}

	private Store findStoreByUuid(String storeUuid) {
		return storeRepository.findByUuid(storeUuid).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
	}
}
