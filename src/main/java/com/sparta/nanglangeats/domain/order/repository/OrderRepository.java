package com.sparta.nanglangeats.domain.order.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.nanglangeats.domain.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
	// 오늘 주문 수 조회
	int countByStoreIdAndCreatedAtBetween(String storeId, LocalDateTime startOfDay, LocalDateTime endOfDay);
	// @Query("SELECT COUNT(o) FROM Order o WHERE o.storeId = :storeId AND DATE(o.createdAt) = CURRENT_DATE")
	// int countTodayOrdersByStoreId(@Param("storeId") String storeId);
}
