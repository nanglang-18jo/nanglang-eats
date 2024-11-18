package com.sparta.nanglangeats.domain.order.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.order.controller.dto.request.OrderCreateRequest;
import com.sparta.nanglangeats.domain.order.controller.dto.response.OrderCreateResponse;
import com.sparta.nanglangeats.domain.order.entity.Order;
import com.sparta.nanglangeats.domain.order.entity.OrderProduct;
import com.sparta.nanglangeats.domain.order.repository.OrderProductRepository;
import com.sparta.nanglangeats.domain.order.repository.OrderRepository;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.exception.CustomFieldError;
import com.sparta.nanglangeats.global.common.exception.ErrorCode;
import com.sparta.nanglangeats.global.common.exception.ParameterException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderProductRepository orderProductRepository;

	@Transactional
	public OrderCreateResponse createOrder(@Valid OrderCreateRequest request, User user) {
		validateOrderProducts(request.getProducts());

		int todayOrderCount = getTodayOrderCountByStore(request.getStoreId()); // 해당 가게의 오늘 주문 수 조회
		int orderNumber = todayOrderCount + 1;  // 오늘의 주문 순서대로 orderNumber 설정

		Order order = Order.builder()
			.userId(user.getUsername())
			.storeId(request.getStoreId())
			.orderNumber(orderNumber)
			.address(request.getAddress())
			.requirement(request.getRequirement())
			.type(Order.OrderType.valueOf(request.getType().name()))
			.status(Order.OrderStatus.PENDING)
			.totalPrice(request.getTotalPrice())
			.build();
		orderRepository.save(order);

		request.getProducts().forEach(productDto -> {
			OrderProduct orderProduct = OrderProduct.builder()
				.orderId(order.getOrderUuid())
				.productId(productDto.getProductId())
				.quantity(productDto.getQuantity())
				.build();
			orderProductRepository.save(orderProduct);
		});

		return new OrderCreateResponse(order.getOrderId());
	}

	private void validateOrderProducts(List<OrderCreateRequest.OrderProductRequestDto> products) {
		List<CustomFieldError> customFieldErrors = new ArrayList<>();
		for (OrderCreateRequest.OrderProductRequestDto product : products) {
			if (product.getProductId() == null || product.getProductId().isEmpty()) {
				customFieldErrors.add(new CustomFieldError("", ErrorCode.ORDER_PRODUCT_ID_INVALID));
			}
			if (product.getQuantity() <= 0) {
				customFieldErrors.add(
					new CustomFieldError(product.getProductId(), ErrorCode.ORDER_PRODUCT_QUANTITY_INVALID));
			}
		}
		if (!customFieldErrors.isEmpty()) {
			throw new ParameterException(ErrorCode.COMMON_INVALID_PARAMETER, customFieldErrors);
		}
	}

	public int getTodayOrderCountByStore(String storeId) {
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

		return orderRepository.countByStoreIdAndCreatedAtBetween(storeId, startOfDay, endOfDay);
	}
}
