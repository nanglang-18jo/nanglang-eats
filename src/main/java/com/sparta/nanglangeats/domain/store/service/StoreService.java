package com.sparta.nanglangeats.domain.store.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.nanglangeats.domain.address.entity.CommonAddress;
import com.sparta.nanglangeats.domain.address.repository.CommonAddressRepository;
import com.sparta.nanglangeats.domain.address.service.GeocodingService;
import com.sparta.nanglangeats.domain.image.service.dto.ImageResponse;
import com.sparta.nanglangeats.domain.store.controller.dto.request.StoreCreateRequest;
import com.sparta.nanglangeats.domain.store.controller.dto.response.StoreCreateResponse;
import com.sparta.nanglangeats.domain.store.entity.Category;
import com.sparta.nanglangeats.domain.store.entity.Store;
import com.sparta.nanglangeats.domain.store.entity.StoreImage;
import com.sparta.nanglangeats.domain.store.repository.CategoryRepository;
import com.sparta.nanglangeats.domain.store.repository.StoreRepository;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;
	private final CommonAddressRepository commonAddressRepository;
	private final GeocodingService geocodingService;
	private final StoreImageService storeImageService;

	@Transactional
	public StoreCreateResponse insertStore(StoreCreateRequest request, User user) {
		// 권한 확인
		if (!(user.getRole() == UserRole.MASTER || user.getRole() == UserRole.MANAGER))
			throw new CustomException(ErrorCode.ACCESS_DENIED);

		Category category = findCategoryById(request.getCategoryId());
		CommonAddress commonAddress = findCommonAddressByAddress(request.getAddress());

		Store store = Store.builder()
			.category(category)
			.user(user)
			.name(request.getName())
			.openTime(request.getOpenTime())
			.closeTime(request.getCloseTime())
			.commonAddress(commonAddress)
			.addressDetail(request.getAddressDetail())
			.phoneNumber(request.getPhoneNumber())
			.build();

		storeRepository.save(store);

		if(!request.getImages().isEmpty()){
			for (MultipartFile image : request.getImages()) {
				StoreImage storeImage = new StoreImage(store, storeImageService.uploadImage(image));
				store.addStoreImage(storeImage);
			}
		}

		return StoreCreateResponse.builder().storeId(store.getUuid().toString()).build();
	}

	/* UTIL */

	private Category findCategoryById(Long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
	}

	private CommonAddress findCommonAddressByAddress(String address) {
		return commonAddressRepository.findByAddress(address).orElse(
			geocodingService.getCoordinates(address)
		);
	}
}
