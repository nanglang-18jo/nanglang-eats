package com.sparta.nanglangeats.domain.product.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.nanglangeats.domain.image.entity.Image;
import com.sparta.nanglangeats.domain.image.enums.ImageCategory;
import com.sparta.nanglangeats.domain.image.repository.ImageRepository;
import com.sparta.nanglangeats.domain.image.service.dto.ImageService;
import com.sparta.nanglangeats.domain.product.controller.dto.request.ProductRequest;
import com.sparta.nanglangeats.domain.product.controller.dto.response.ProductResponse;
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
	public ProductResponse createProduct(ProductRequest request, User user) {
		Store store = findStoreByUuid(request.getStoreUuid());
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
		return ProductResponse.builder().productUuid(product.getUuid()).build();
	}

	@Transactional
	public ProductResponse updateProduct(String uuid, ProductRequest request, User user) {
		Product product = findProductByUuid(uuid);
		validateUser(product.getStore(), user);

		product.update(request);

		imageService.deleteAllImages(ImageCategory.PRODUCT_IMAGE, product.getId());
		imageService.uploadAllImages(request.getImages(), ImageCategory.PRODUCT_IMAGE, product.getId());

		return ProductResponse.builder().productUuid(product.getUuid()).build();
	}

	@Transactional
	public void deleteProduct(String uuid, User user) {
		Product product = findProductByUuid(uuid);
		validateUser(product.getStore(), user);

		product.delete(user.getUsername());
	}

	/* UTIL */
	private void validateUser(Store store, User user) {
		if (user.getRole() == UserRole.OWNER && !store.getOwner().equals(user))
			throw new CustomException(ErrorCode.ACCESS_DENIED);
	}

	private Product findProductByUuid(String uuid) {
		return productRepository.findByUuid(uuid).orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
	}

	private Store findStoreByUuid(String uuid) {
		return storeRepository.findByUuid(uuid).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
	}
}
