package com.sparta.nanglangeats.domain.store.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.address.entity.CommonAddress;
import com.sparta.nanglangeats.domain.address.service.CommonAddressService;
import com.sparta.nanglangeats.domain.image.entity.Image;
import com.sparta.nanglangeats.domain.image.enums.ImageCategory;
import com.sparta.nanglangeats.domain.image.repository.ImageRepository;
import com.sparta.nanglangeats.domain.image.service.ImageService;
import com.sparta.nanglangeats.domain.store.controller.dto.request.StoreRequest;
import com.sparta.nanglangeats.domain.store.controller.dto.response.StoreDetailResponse;
import com.sparta.nanglangeats.domain.store.controller.dto.response.StoreListResponse;
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
	public StoreResponse createStore(StoreRequest request) {
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

		return StoreResponse.builder().storeUuid(store.getUuid()).build();
	}

	@Transactional
	public StoreResponse updateStore(String uuid, StoreRequest request, User user) {
		Store store = findStoreByUuid(uuid);

		if (user.getRole().equals(UserRole.OWNER) && !store.getOwner().equals(user))
			throw new CustomException(ErrorCode.ACCESS_DENIED);

		Category category = findCategoryById(request.getCategoryId());
		CommonAddress commonAddress = commonAddressService.findCommonAddressByAddress(request.getAddress());
		store.update(request, category, commonAddress);

		imageService.hardDeleteAllImages(ImageCategory.STORE_IMAGE, store.getId());
		imageService.uploadAllImages(request.getImages(), ImageCategory.STORE_IMAGE, store.getId());

		return StoreResponse.builder().storeUuid(store.getUuid()).build();
	}

	@Transactional
	public void deleteStore(String uuid, User user) {
		Store store = findStoreByUuid(uuid);

		if (user.getRole().equals(UserRole.OWNER) && !store.getOwner().equals(user))
			throw new CustomException(ErrorCode.ACCESS_DENIED);
		store.delete(user.getUsername());

		imageService.softDeleteAllImages(ImageCategory.STORE_IMAGE, store.getId(), user.getUsername());
	}

	public StoreDetailResponse getStoreDetail(String uuid){
		Store store = findStoreByUuid(uuid);
		List<String> imageUrls = imageRepository.findUrlsByImageCategoryAndContentId(ImageCategory.STORE_IMAGE, store.getId());
		return StoreDetailResponse.builder().store(store).imageUrls(imageUrls).build();
	}

	public Page<StoreListResponse> getStoresList(Long categoryId, int page, int size, String sortBy, String direction) {
		Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
		Pageable pageable = PageRequest.of(page, size, sort);

		Page<Store> stores = storeRepository.findAllByCategoryId(categoryId, pageable);

		List<Long> storeIds = stores.stream()
			.map(Store::getId)
			.toList();

		List<Image> images = imageRepository.findByContentIdInAndImageCategory(storeIds, ImageCategory.STORE_IMAGE);

		Map<Long, List<String>> imageUrlMap = images.stream()
			.collect(Collectors.groupingBy(
				Image::getContentId,
				Collectors.mapping(Image::getUrl, Collectors.toList())
			));

		// Store 엔티티와 이미지 리스트 매핑
		List<StoreListResponse> storeResponses = stores.stream()
			.map(store -> new StoreListResponse(
				store.getUuid(),
				store.getName(),
				store.getRating(),
				store.getReviewCount(),
				imageUrlMap.getOrDefault(store.getId(), Collections.emptyList()) // 해당 Store의 이미지 리스트
			))
			.toList();

		return new PageImpl<>(storeResponses, pageable, stores.getTotalElements());
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

	private Store findStoreByUuid(String uuid) {
		return storeRepository.findByUuid(uuid).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
	}
}
