package com.sparta.nanglangeats.domain.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.nanglangeats.domain.order.entity.Order;
import com.sparta.nanglangeats.domain.order.entity.OrderProduct;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
	// 주문 상품 삭제
	void deleteByOrder(Order order);
}