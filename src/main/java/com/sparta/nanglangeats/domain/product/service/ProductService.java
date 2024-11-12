package com.sparta.nanglangeats.domain.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.nanglangeats.domain.image.entity.Image;
import com.sparta.nanglangeats.domain.image.enums.ImageCategory;
import com.sparta.nanglangeats.domain.image.repository.ImageRepository;
import com.sparta.nanglangeats.domain.image.service.dto.ImageService;
import com.sparta.nanglangeats.domain.product.controller.dto.request.ProductCreateRequest;
import com.sparta.nanglangeats.domain.product.controller.dto.response.ProductCreateResponse;
import com.sparta.nanglangeats.domain.product.entity.Product;
import com.sparta.nanglangeats.domain.product.repository.ProductRepository;
import com.sparta.nanglangeats.domain.store.entity.Store;
import com.sparta.nanglangeats.domain.store.repository.StoreRepository;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

	private final StoreRepository storeRepository;
	private final ProductRepository productRepository;
	private final ImageService imageService;
	private final ImageRepository imageRepository;

	@Transactional
	public ProductCreateResponse createProduct(ProductCreateRequest request, User user) {
		Store store = storeRepository.findById(request.getStoreId())
			.orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
		validateUser(store, user);

		Product product = Product.builder()
			.store(store)
			.name(request.getName())
			.description(request.getDescription())
			.price(request.getPrice())
			.build();

		productRepository.save(product);

		if (!request.getImages().isEmpty()) {
			for (MultipartFile image : request.getImages()) {
				Image productImage = new Image(imageService.uploadImage(image, "product-images"), product.getId(),
					ImageCategory.PRODUCT_IMAGE);
				imageRepository.save(productImage);
			}
		}
		return ProductCreateResponse.builder().productId(product.getUuid().toString()).build();
	}

	/* UTIL */
	private void validateUser(Store store, User user) {
		if (!store.getOwner().equals(user) && !(user.getRole() == UserRole.MANAGER || user.getRole() == UserRole.MASTER))
			throw new CustomException(ErrorCode.ACCESS_DENIED);
	}
}
