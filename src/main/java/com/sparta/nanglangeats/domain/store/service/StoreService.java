package com.sparta.nanglangeats.domain.store.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.nanglangeats.domain.address.entity.CommonAddress;
import com.sparta.nanglangeats.domain.address.service.CommonAddressService;
import com.sparta.nanglangeats.domain.image.entity.Image;
import com.sparta.nanglangeats.domain.image.enums.ImageCategory;
import com.sparta.nanglangeats.domain.image.repository.ImageRepository;
import com.sparta.nanglangeats.domain.image.service.dto.ImageService;
import com.sparta.nanglangeats.domain.store.controller.dto.request.StoreRequest;
import com.sparta.nanglangeats.domain.store.controller.dto.response.StoreResponse;
import com.sparta.nanglangeats.domain.store.entity.Category;
import com.sparta.nanglangeats.domain.store.entity.Store;
import com.sparta.nanglangeats.domain.store.repository.CategoryRepository;
import com.sparta.nanglangeats.domain.store.repository.StoreRepository;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.domain.user.repository.UserRepository;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;
	private final ImageService imageService;
	private final ImageRepository imageRepository;
	private final CommonAddressService commonAddressService;
	private final UserRepository userRepository;

	@Transactional
	public StoreResponse createStore(StoreRequest request, User user) {
		// 권한 확인
		if (!(user.getRole() == UserRole.MASTER || user.getRole() == UserRole.MANAGER))
			throw new CustomException(ErrorCode.ACCESS_DENIED);

		User owner = validateOwner(request.getOwnerId());
		Category category = findCategoryById(request.getCategoryId());
		CommonAddress commonAddress = commonAddressService.findCommonAddressByAddress(request.getAddress());

		Store store = Store.builder()
			.category(category)
			.owner(owner)
			.name(request.getName())
			.openTime(request.getOpenTime())
			.closeTime(request.getCloseTime())
			.commonAddress(commonAddress)
			.addressDetail(request.getAddressDetail())
			.phoneNumber(request.getPhoneNumber())
			.build();

		storeRepository.save(store);

		if (!request.getImages().isEmpty()) {
			imageService.uploadAllImages(request.getImages(), ImageCategory.STORE_IMAGE, store.getId());
		}

		return StoreResponse.builder().storeId(store.getUuid()).build();
	}

	@Transactional
	public StoreResponse updateStore(Long storeId, StoreRequest request, User user) {
		Store store = findStoreById(storeId);

		if (user.getRole().equals(UserRole.OWNER) && !store.getOwner().equals(user))
			throw new CustomException(ErrorCode.ACCESS_DENIED);

		Category category = findCategoryById(request.getCategoryId());
		CommonAddress commonAddress = commonAddressService.findCommonAddressByAddress(request.getAddress());
		store.update(request, category, commonAddress);

		imageService.deleteAllImages(ImageCategory.STORE_IMAGE, store.getId());
		imageService.uploadAllImages(request.getImages(), ImageCategory.STORE_IMAGE, store.getId());

		return StoreResponse.builder().storeId(store.getUuid()).build();
	}

	@Transactional
	public void deleteStore(Long storeId, User user) {
		Store store = findStoreById(storeId);

		if (user.getRole().equals(UserRole.OWNER) && !store.getOwner().equals(user))
			throw new CustomException(ErrorCode.ACCESS_DENIED);

		store.delete(user.getUsername());
	}

	/* UTIL */
	private User validateOwner(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		if (!user.getRole().equals(UserRole.OWNER))
			throw new CustomException(ErrorCode.USER_ROLE_NOT_OWNER);
		return user;
	}

	private Category findCategoryById(Long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
	}

	private Store findStoreById(Long id) {
		return storeRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
	}
}
