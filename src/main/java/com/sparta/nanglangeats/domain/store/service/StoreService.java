package com.sparta.nanglangeats.domain.store.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.address.entity.CommonAddress;
import com.sparta.nanglangeats.domain.address.service.CommonAddressService;
import com.sparta.nanglangeats.domain.image.enums.ImageCategory;
import com.sparta.nanglangeats.domain.image.repository.ImageRepository;
import com.sparta.nanglangeats.domain.image.service.ImageService;
import com.sparta.nanglangeats.domain.image.service.dto.ImageResponse;
import com.sparta.nanglangeats.domain.image.util.S3Util;
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
	private static final int PAGE_SIZE = 10;
	private final StoreRepository storeRepository;
	private final CategoryRepository categoryRepository;
	private final ImageService imageService;
	private final ImageRepository imageRepository;
	private final CommonAddressService commonAddressService;
	private final UserRepository userRepository;
	private final S3Util s3Util;

	@Transactional
	public StoreResponse createStore(StoreRequest request) {
		User owner = validateOwner(request.getOwnerId());
		Category category = findCategoryById(request.getCategoryId());
		CommonAddress commonAddress = commonAddressService.findCommonAddressByAddress(request.getAddress());

		ImageResponse thumbnailResponse = null;

		if (request.getThumbnail() != null) {
			thumbnailResponse = s3Util.uploadFile(request.getThumbnail(), "store-thumbnails");
		}

		Store store = Store.builder()
			.category(category)
			.owner(owner)
			.name(request.getName())
			.openTime(request.getOpenTime())
			.closeTime(request.getCloseTime())
			.commonAddress(commonAddress)
			.addressDetail(request.getAddressDetail())
			.phoneNumber(request.getPhoneNumber())
			.thumbnailName(thumbnailResponse != null ? thumbnailResponse.getFileName() : null)
			.thumbnailUrl(thumbnailResponse != null ? thumbnailResponse.getUrl() : null)
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

		ImageResponse thumbnailResponse = null;
		if (request.getThumbnail() != null) {
			if (store.getThumbnailName() != null)
				s3Util.deleteFile(store.getThumbnailName());
			thumbnailResponse = s3Util.uploadFile(request.getThumbnail(), "store-thumbnails");
		}

		store.update(request, category, commonAddress, thumbnailResponse);

		imageService.hardDeleteAllImages(ImageCategory.STORE_IMAGE, store.getId());
		if (request.getImages() != null)
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

	public StoreDetailResponse getStoreDetail(String uuid) {
		Store store = findStoreByUuid(uuid);
		validateStore(store);

		List<String> imageUrls = imageRepository.findUrlsByImageCategoryAndContentId(ImageCategory.STORE_IMAGE,
			store.getId());
		return StoreDetailResponse.builder().store(store).imageUrls(imageUrls).build();
	}

	public Page<StoreListResponse> getStoresList(Long categoryId, int page, String sortBy, String direction) {
		Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

		Page<Store> stores = storeRepository.findAllByCategoryId(categoryId, pageable);

		return stores.map(store -> StoreListResponse.builder().store(store).build());
	}

	public Page<StoreListResponse> searchStore(String keyword, int page) {
		Sort sort = Sort.by(Sort.Direction.ASC, "name");
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

		Page<Store> stores = storeRepository.searchStoreByKeyword(keyword, pageable);

		return stores.map(store -> StoreListResponse.builder().store(store).build());
	}

	/* UTIL */
	private User validateOwner(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		if (!user.getRole().equals(UserRole.OWNER))
			throw new CustomException(ErrorCode.USER_ROLE_NOT_OWNER);
		return user;
	}

	private void validateStore(Store store) {
		if (!store.getIsActive())
			throw new CustomException(ErrorCode.STORE_NOT_FOUND);
	}

	private Category findCategoryById(Long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
	}

	private Store findStoreByUuid(String uuid) {
		return storeRepository.findByUuid(uuid).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
	}
}
