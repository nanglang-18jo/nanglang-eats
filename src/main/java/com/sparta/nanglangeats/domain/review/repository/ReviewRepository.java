package com.sparta.nanglangeats.domain.review.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sparta.nanglangeats.domain.order.entity.Order;
import com.sparta.nanglangeats.domain.review.entity.Review;
import com.sparta.nanglangeats.domain.store.entity.Store;
import com.sparta.nanglangeats.domain.user.entity.User;

public interface ReviewRepository  extends JpaRepository<Review, Long> {

	boolean existsByOrderOrderId(Long orderId);

	Optional<Review> findByUuid(String reviewUuid);

	// // 삭제되지 않은 특정 음식점의 리뷰 리스트 조회
	// List<Review> findByStoreAndDeletedFalse(Store store);
	//
	// // 삭제되지 않은 특정 사용자의 리뷰 리스트 조회
	// List<Review> findByUserAndDeletedFalse(User user);
	//
	// // 삭제되지 않은 특정 주문의 리뷰 조회
	// Review findByOrderAndDeletedFalse(Order order);
	// @Query("SELECT AVG(r.rating) FROM Review r WHERE r.store.storeId = :storeId")
	// Optional<Double> findAverageRatingByStoreId(UUID storeId);
	//
	// List<Review> findByStore(Store store);


}
