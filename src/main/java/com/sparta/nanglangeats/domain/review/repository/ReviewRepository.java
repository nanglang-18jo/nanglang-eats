package com.sparta.nanglangeats.domain.review.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.nanglangeats.domain.review.entity.Review;

public interface ReviewRepository  extends JpaRepository<Review, Long> {

	boolean existsByOrderOrderIdAndIsActiveTrue(Long orderId);

	Optional<Review> findByUuidAndIsActiveTrue(String reviewUuid);

	Page<Review> findAllByStoreIdAndIsActiveTrueOrderByCreatedAtDesc(Long storeId, Pageable pageable);

}
