package com.sparta.nanglangeats.domain.order.service;

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

		Order order = Order.builder()
			.userId(user.getUsername())
			.storeId(request.getStoreId())
			.address(request.getAddress())
			.requirement(request.getRequirement())
			.type(Order.OrderType.valueOf(request.getType().name()))
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
		List<CustomFieldError> fieldErrors = new ArrayList<>();
		for (OrderCreateRequest.OrderProductRequestDto product : products) {
			if (product.getQuantity() <= 0) {
				fieldErrors.add(new CustomFieldError(product.getProductId(), ErrorCode.COMMON_INVALID_PARAMETER));
			}
		}
		if (!fieldErrors.isEmpty()) {
			throw new ParameterException(ErrorCode.COMMON_INVALID_PARAMETER, fieldErrors);
		}
	}

	private void validateUser(User user) {
		//
	}
}