package com.sparta.nanglangeats.domain.order.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sparta.nanglangeats.domain.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
	// 오늘 주문 수 조회
	int countByStoreIdAndCreatedAtBetween(String storeId, LocalDateTime startOfDay, LocalDateTime endOfDay);

	Optional<Order> findByOrderUuid(String orderUuId);
}
