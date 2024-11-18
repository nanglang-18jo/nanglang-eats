package com.sparta.nanglangeats.domain.delivery_address.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.address.entity.CommonAddress;
import com.sparta.nanglangeats.domain.address.service.CommonAddressService;
import com.sparta.nanglangeats.domain.delivery_address.controller.dto.request.DeliveryAddressCreateRequest;
import com.sparta.nanglangeats.domain.delivery_address.entity.DeliveryAddress;
import com.sparta.nanglangeats.domain.delivery_address.repository.DeliveryAddressRepository;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.service.UserService;

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
}
