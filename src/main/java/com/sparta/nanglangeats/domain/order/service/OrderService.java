package com.sparta.nanglangeats.domain.order.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.order.controller.dto.request.OrderCreateRequest;
import com.sparta.nanglangeats.domain.order.controller.dto.request.OrderUpdateRequest;
import com.sparta.nanglangeats.domain.order.controller.dto.request.ProductRequestDto;
import com.sparta.nanglangeats.domain.order.controller.dto.response.OrderCreateResponse;
import com.sparta.nanglangeats.domain.order.controller.dto.response.OrderDetailResponse;
import com.sparta.nanglangeats.domain.order.controller.dto.response.OrderSummaryResponse;
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

	// 주문 등록
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
				.price(productDto.getPrice())
				.build();
			orderProductRepository.save(orderProduct);
		});

		return new OrderCreateResponse(order.getOrderId());
	}

	// 주문 수정
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

	// 주문 상태 수정
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

	// 주문 취소
	@Transactional
	public void cancelOrder(Long orderId, User user) {
		// 주문 조회
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

		// 주문이 PENDING 상태가 아닌 경우 예외 처리
		if (!order.getStatus().equals(OrderStatus.PENDING)) {
			throw new CustomException(ErrorCode.ORDER_NOT_CANCELABLE);
		}

		// 5분 이내인지 확인
		if (order.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5))) {
			throw new CustomException(ErrorCode.ORDER_CANCEL_TIME_EXCEEDED);
		}

		// 권한별 사용자 확인
		switch (user.getRole()) {
			case CUSTOMER:
				if (!order.getUserId().equals(user.getId())) {
					throw new CustomException(ErrorCode.ORDER_UPDATE_FORBIDDEN);
				}
				break;
			case OWNER:
				validateStoreOwner(order.getStoreId(), user.getId());
				break;
			case MANAGER:
				// MANAGER는 취소 가능, 추가 검증 없음
				break;
			default:
				throw new CustomException(ErrorCode.ORDER_UPDATE_FORBIDDEN);
		}

		// 상태를 CANCELED로 변경
		order.setStatus(OrderStatus.CANCELED);
		orderRepository.save(order);
	}

	// 주문 삭제
	@Transactional
	public void deleteOrder(Long orderId, User user) {
		// 주문 조회
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

		// 상태 확인 (삭제 가능한 상태인지 검증)
		if (!OrderStatus.CANCELED.equals(order.getStatus())) {
			throw new CustomException(ErrorCode.ORDER_NOT_DELETABLE);
		}

		// Soft Delete
		order.delete(user.getId());
		orderRepository.save(order); // 상태 저장
	}

	// 주문 상세 조회
	@Transactional(readOnly = true)
	public OrderDetailResponse getOrderDetail(Long orderId, User user) {
		// 주문 조회
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

		switch (user.getRole()) {
			case CUSTOMER:
				if (!order.getUserId().equals(user.getId())) {
					throw new CustomException(ErrorCode.ACCESS_DENIED);
				}
				break;
			case OWNER:
				validateStoreOwner(order.getStoreId(), user.getId());
				break;
			case MANAGER:
				break; // MANAGER는 모든 주문 조회 가능
			default:
				throw new CustomException(ErrorCode.ACCESS_DENIED);
		}

		return new OrderDetailResponse(order);
	}

	// 주문 목록 조회
	@Transactional(readOnly = true)
	public Object getOrderList(User user, int page, int size, String sortBy, String status, String search) {
		if (size != 10 && size != 30 && size != 50) {
			size = 10; // 허용되지 않은 size 값은 기본값으로 설정
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
		Specification<Order> spec = Specification.where(null); // 동적 쿼리

		// 사용자 역할에 따른 필터링
		switch (user.getRole()) {
			case CUSTOMER: // 자신이 등록한 주문만 조회
				spec = spec.and((root, query, cb) -> cb.equal(root.get("userId"), user.getId()));
				break;
			case OWNER: // 자신의 가게 주문만 조회
				// TODO: storeService.getStoreIdsByOwnerId(user.getId()) 구현 필요
				//List<String> storeIds = storeService.getStoreIdsByOwnerId(user.getId());
				//spec = spec.and((root, query, cb) -> root.get("storeId").in(storeIds));
				break;
			case MANAGER:
				break; // MANAGER는 모든 주문 조회 가능
			default:
				throw new CustomException(ErrorCode.ACCESS_DENIED);
		}

		// 상태 필터링
		if (status != null) {
			spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), OrderStatus.valueOf(status)));
		}

		// 검색
		if (search != null && !search.isEmpty()) {
			spec = spec.and((root, query, cb) -> cb.like(root.get("address"), "%" + search + "%"));
		}

		Page<Order> orders = orderRepository.findAll(spec, pageable);
		return orders.map(OrderSummaryResponse::new);
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
