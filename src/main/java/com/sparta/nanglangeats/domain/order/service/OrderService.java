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
import com.sparta.nanglangeats.domain.order.enums.OrderStatus;
import com.sparta.nanglangeats.domain.order.enums.OrderType;
import com.sparta.nanglangeats.domain.order.repository.OrderProductRepository;
import com.sparta.nanglangeats.domain.order.repository.OrderRepository;
import com.sparta.nanglangeats.domain.store.service.StoreService;
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
	private final StoreService storeService;

	@Transactional
	public OrderCreateResponse createOrder(@Valid OrderCreateRequest request, User user) {

		// 현장 주문(ONSITE)인 경우, 요청 사용자가 해당 가게의 주인인지 확인
		if (request.getType().equals(OrderType.ONSITE)) {
			validateOwnerForOnsiteOrder(request.getStoreId(), user.getId());
		}

		// 주문 상품 검증
		validateOrderProducts(request.getProducts());

		// 해당 가게의 오늘 주문 수 조회 및 orderNumber 설정
		int todayOrderCount = getTodayOrderCountByStore(request.getStoreId());
		int orderNumber = todayOrderCount + 1;

		// 주문 저장
		Order order = Order.builder()
			.userId(user.getUsername())
			.storeId(request.getStoreId())
			.orderNumber(orderNumber)
			.address(request.getAddress())
			.requirement(request.getRequirement())
			.type(OrderType.valueOf(request.getType().name()))
			.status(OrderStatus.PENDING)
			.totalPrice(request.getTotalPrice())
			.build();
		orderRepository.save(order);

		// 주문 상품 저장
		request.getProducts().forEach(productDto -> {
			OrderProduct orderProduct = OrderProduct.builder()
				.order(order)
				.productId(productDto.getProductId())
				.quantity(productDto.getQuantity())
				.build();
			orderProductRepository.save(orderProduct);
		});

		return new OrderCreateResponse(order.getOrderId());
	}

	// 주문 상품 검증
	private void validateOrderProducts(List<OrderCreateRequest.OrderProductRequestDto> products) {
		List<CustomFieldError> customFieldErrors = new ArrayList<>();
		for (OrderCreateRequest.OrderProductRequestDto product : products) {
			// 상품 ID 검증
			if (product.getProductId() == null || product.getProductId().isEmpty()) {
				customFieldErrors.add(new CustomFieldError("", ErrorCode.ORDER_PRODUCT_ID_INVALID));
			}
			// 상품 수량 검증
			if (product.getQuantity() <= 0) {
				customFieldErrors.add(
					new CustomFieldError(product.getProductId(), ErrorCode.ORDER_PRODUCT_QUANTITY_INVALID));
			}
		}
		if (!customFieldErrors.isEmpty()) {
			throw new ParameterException(ErrorCode.COMMON_INVALID_PARAMETER, customFieldErrors);
		}
	}

	// 현장 주문(ONSITE)인 경우, 요청 사용자가 해당 가게의 주인인지 확인
	public void validateOwnerForOnsiteOrder(String storeId, Long userId) {
		// 가게 주인의 ID를 가져오고, 없거나 잘못된 경우 예외 처리
		// TODO: StoreService에 getStoreOwnerId() 구현 필요
		/*
		String storeOwnerId = Optional.ofNullable(storeService.getStoreOwnerId(request.getStoreId()))
			.filter(id -> id instanceof String)
			.map(String.class::cast)
			.orElseThrow(() -> new CustomException("가게 주인을 찾을 수 없거나 잘못된 유형입니다.",
				ErrorCode.STORE_OWNER_NOT_FOUND));

		// 요청 사용자가 가게 주인이 아닌 경우 예외 처리
		if (!storeOwnerId.equals(user.getId())) {
			throw new CustomException("현장 주문은 해당 가게 주인만 가능합니다.", ErrorCode.ONSITE_ORDER_NOT_ALLOWED);
		}
		*/
	}

	// 해당 가게의 오늘 주문 수 조회
	public int getTodayOrderCountByStore(String storeId) {
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay(); // 오늘 시작 시간
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1); // 오늘 종료 시간

		return orderRepository.countByStoreIdAndCreatedAtBetween(storeId, startOfDay, endOfDay);
	}
}
