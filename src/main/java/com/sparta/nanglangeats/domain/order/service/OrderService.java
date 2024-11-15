package com.sparta.nanglangeats.domain.order.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.order.controller.dto.request.OrderCreateRequest;
import com.sparta.nanglangeats.domain.order.controller.dto.request.OrderUpdateRequest;
import com.sparta.nanglangeats.domain.order.controller.dto.request.ProductRequestDto;
import com.sparta.nanglangeats.domain.order.controller.dto.response.OrderCreateResponse;
import com.sparta.nanglangeats.domain.order.controller.dto.response.OrderUpdateResponse;
import com.sparta.nanglangeats.domain.order.entity.Order;
import com.sparta.nanglangeats.domain.order.entity.OrderProduct;
import com.sparta.nanglangeats.domain.order.enums.OrderStatus;
import com.sparta.nanglangeats.domain.order.enums.OrderType;
import com.sparta.nanglangeats.domain.order.repository.OrderProductRepository;
import com.sparta.nanglangeats.domain.order.repository.OrderRepository;
import com.sparta.nanglangeats.domain.store.service.StoreService;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.exception.CustomException;
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
			validateStoreOwner(request.getStoreId(), user.getId());
		}

		// 주문 상품 검증
		validateOrderProducts(request.getProducts());

		// 해당 가게의 오늘 주문 수 조회 및 orderNumber 설정
		int todayOrderCount = getTodayOrderCountByStore(request.getStoreId());
		int orderNumber = todayOrderCount + 1;

		// 주문 저장
		Order order = Order.builder()
			.userId(user.getId())
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

	@Transactional
	public OrderUpdateResponse updateOrder(Long orderId, OrderUpdateRequest request, User user) {
		// 주문 존재 여부 확인
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

		// 권한 확인
		if (!order.getUserId().equals(user.getId())) {
			System.out.println("order.getUserId() = " + order.getUserId());
			System.out.println("user.getId() = " + user.getId());
			throw new CustomException(ErrorCode.ORDER_UPDATE_FORBIDDEN);
		}

		// 주문 상품 검증
		validateOrderProducts(request.getProducts());

		// 주문 정보 수정
		order.setAddress(request.getAddress());
		order.setRequirement(request.getRequirement());
		order.setType(OrderType.valueOf(request.getType().name()));
		order.setTotalPrice(request.getTotalPrice());

		// 주문 상품 수정 (기존 상품 삭제 후 새로 추가)
		orderProductRepository.deleteByOrder(order);
		request.getProducts().forEach(productDto -> {
			OrderProduct orderProduct = OrderProduct.builder()
				.order(order)
				.productId(productDto.getProductId())
				.quantity(productDto.getQuantity())
				.build();
			orderProductRepository.save(orderProduct);
		});

		return new OrderUpdateResponse(order.getOrderId());
	}

	@Transactional
	public void updateOrderStatus(Long orderId, User user, OrderStatus newStatus) {
		// 주문 조회
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

		// 가게 주인 검증
		validateStoreOwner(order.getStoreId(), user.getId());

		// 상태 변경 검증
		if (order.getStatus() == OrderStatus.PENDING && newStatus == OrderStatus.CANCELED) {
			// 취소 가능 시간 확인 (5분 제한)
			if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5))) {
				throw new CustomException(ErrorCode.ORDER_CANCEL_TIME_EXCEEDED);
			}
		} else if (order.getStatus() != OrderStatus.PENDING) {
			throw new CustomException(ErrorCode.ORDER_STATUS_CHANGE_INVALID);
		}

		// 상태 업데이트
		order.updateStatus(newStatus);
	}

	// 주문 상품 검증
	private <T extends ProductRequestDto> void validateOrderProducts(List<T> products) {
		List<CustomFieldError> customFieldErrors = new ArrayList<>();
		for (T product : products) {
			// 상품 ID 검증
			if (product.getProductId() == null || product.getProductId().isEmpty()) {
				customFieldErrors.add(new CustomFieldError("", ErrorCode.ORDER_PRODUCT_ID_INVALID));
			}
			// 상품 수량 검증
			if (product.getQuantity() == null || product.getQuantity() <= 0) {
				customFieldErrors.add(
					new CustomFieldError(product.getProductId(), ErrorCode.ORDER_PRODUCT_QUANTITY_INVALID));
			}
		}
		if (!customFieldErrors.isEmpty()) {
			throw new ParameterException(ErrorCode.COMMON_INVALID_PARAMETER, customFieldErrors);
		}
	}

	// 요청 사용자가 해당 가게의 주인인지 확인
	public void validateStoreOwner(String storeId, Long userId) {
		// 가게 주인의 ID를 가져오고, 없거나 잘못된 경우 예외 처리
		// TODO: StoreService에 getStoreOwnerId() 구현 필요
		/*
		String storeOwnerId = Optional.ofNullable(storeService.getStoreOwnerId(request.getStoreId()))
			.filter(id -> id instanceof String)
			.map(String.class::cast)
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_OWNER_NOT_FOUND));

		// 요청 사용자가 가게 주인이 아닌 경우 예외 처리
		if (!storeOwnerId.equals(user.getId())) {
			throw new CustomException(ErrorCode.ORDER_UPDATE_FORBIDDEN);
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
