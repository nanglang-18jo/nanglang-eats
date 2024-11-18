package com.sparta.nanglangeats.domain.user.service;

import static com.sparta.nanglangeats.global.common.exception.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.address.entity.CommonAddress;
import com.sparta.nanglangeats.domain.address.service.CommonAddressService;
import com.sparta.nanglangeats.domain.user.controller.dto.request.DeliveryAddressCreateRequest;
import com.sparta.nanglangeats.domain.user.controller.dto.request.DeliveryAddressUpdateRequest;
import com.sparta.nanglangeats.domain.user.entity.DeliveryAddress;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.repository.DeliveryAddressRepository;
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

		updatePreviousDeliveryStatus(findUser, request.getIsRecentDelivery());

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

		updatePreviousDeliveryStatus(user, request.getIsRecentDelivery());

		CommonAddress commonAddress = commonAddressService.findCommonAddressByAddress(request.getAddress());
		deliveryAddress.update(commonAddress, request);
		return deliveryAddress.getId();
	}

	@Transactional
	public void deleteDeliveryAddress(User user, Long deliveryAddressId) {
		DeliveryAddress deliveryAddress = getDeliveryAddressRepositoryById(deliveryAddressId);
		validateUser(user.getId(), deliveryAddress.getUser().getId());
		deliveryAddressRepository.delete(deliveryAddress);
	}

	@Transactional(readOnly = true)
	public DeliveryAddress getDeliveryAddressRepositoryById(Long deliveryAddressId) {
		return deliveryAddressRepository.findById(deliveryAddressId)
			.orElseThrow(() -> new CustomException(DELIVERY_ADDRESS_NOT_FOUND));
	}

	@Transactional
	protected void updatePreviousDeliveryStatus(User user, Boolean isRecentDelivery) {
		if (isRecentDelivery) {
			deliveryAddressRepository.findByUserAndIsRecentDeliveryTrue(user)
				.ifPresent(entity -> entity.updateIsRecentDelivery(false));
		}
	}

	private void validateUser(Long userId, Long deliveryAddressUserId) {
		if (!userId.equals(deliveryAddressUserId)) {
			throw new CustomException(ACCESS_DENIED);
		}
	}
}
