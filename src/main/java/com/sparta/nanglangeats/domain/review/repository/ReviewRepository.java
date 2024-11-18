package com.sparta.nanglangeats.domain.review.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sparta.nanglangeats.domain.review.entity.Review;
import com.sparta.nanglangeats.domain.user.entity.User;

public interface ReviewRepository  extends JpaRepository<Review, Long> {

	boolean existsByOrderOrderIdAndIsActiveTrue(Long orderId);

	Optional<Review> findByUuidAndIsActiveTrue(String reviewUuid);

	Page<Review> findAllByStoreIdAndIsActiveTrueOrderByCreatedAtDesc(Long storeId, Pageable pageable);

	@Query("SELECT r FROM Review r JOIN FETCH r.store WHERE r.user = :user")
	List<Review> findAllMyReviews(User user, Pageable pageable);
}
