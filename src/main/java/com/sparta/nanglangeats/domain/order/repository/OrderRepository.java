package com.sparta.nanglangeats.domain.order.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sparta.nanglangeats.domain.order.entity.Order;

import jakarta.validation.constraints.NotNull;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
	// 오늘 주문 수 조회
	int countByStoreIdAndCreatedAtBetween(String storeId, LocalDateTime startOfDay, LocalDateTime endOfDay);

	Optional<Order> findByOrderUuid(@NotNull(message = "주문 ID는 필수 값입니다.") String orderUuId);
}
