package com.sparta.nanglangeats.domain.delivery_address.service;

import static com.sparta.nanglangeats.global.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.address.entity.CommonAddress;
import com.sparta.nanglangeats.domain.address.service.CommonAddressService;
import com.sparta.nanglangeats.domain.delivery_address.controller.dto.request.DeliveryAddressCreateRequest;
import com.sparta.nanglangeats.domain.delivery_address.controller.dto.request.DeliveryAddressUpdateRequest;
import com.sparta.nanglangeats.domain.delivery_address.entity.DeliveryAddress;
import com.sparta.nanglangeats.domain.delivery_address.repository.DeliveryAddressRepository;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.service.UserService;
import com.sparta.nanglangeats.global.common.exception.CustomException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryAddressService {

	private final UserService userService;
	private final CommonAddressService commonAddressService;
	private final DeliveryAddressRepository deliveryAddressRepository;

	@Transactional
	public Long createDeliveryAddress(User user, DeliveryAddressCreateRequest request) {
		User findUser = userService.getUserById(user.getId());
		CommonAddress commonAddress = commonAddressService.findCommonAddressByAddress(request.getAddress());

		return deliveryAddressRepository.save(DeliveryAddress.builder()
				.user(findUser)
				.commonAddress(commonAddress)
				.addressDetail(request.getAddressDetail())
				.alias(request.getAlias())
				.isRecentDelivery(request.getIsRecentDelivery())
				.build())
			.getId();
	}

	@Transactional
	public Long updateDeliveryAddress(User user, Long deliveryAddressId, DeliveryAddressUpdateRequest request) {
		DeliveryAddress deliveryAddress = getDeliveryAddressRepositoryById(deliveryAddressId);
		validateUser(user.getId(), deliveryAddress.getUser().getId());

		CommonAddress commonAddress = commonAddressService.findCommonAddressByAddress(request.getAddress());
		deliveryAddress.update(commonAddress, request);
		return deliveryAddress.getId();
	}

	@Transactional(readOnly = true)
	public DeliveryAddress getDeliveryAddressRepositoryById(Long deliveryAddressId) {
		return deliveryAddressRepository.findById(deliveryAddressId)
			.orElseThrow(() -> new CustomException(DELIVERY_ADDRESS_NOT_FOUND));
	}

	private void validateUser(Long userId, Long deliveryAddressUserId) {
		if (!userId.equals(deliveryAddressUserId)) {
			throw new CustomException(ACCESS_DENIED);
		}
	}
}
