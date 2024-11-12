package com.sparta.nanglangeats.domain.address.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.address.entity.CommonAddress;
import com.sparta.nanglangeats.domain.address.repository.CommonAddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonAddressService {
	private final CommonAddressRepository commonAddressRepository;
	private final GeocodingService geocodingService;

	public CommonAddress findCommonAddressByAddress(String address) {
		return commonAddressRepository.findByAddress(address).orElse(
			geocodingService.getCoordinates(address)
		);
	}
}
