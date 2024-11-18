package com.sparta.nanglangeats.domain.product.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.image.enums.ImageCategory;
import com.sparta.nanglangeats.domain.image.repository.ImageRepository;
import com.sparta.nanglangeats.domain.image.service.ImageService;
import com.sparta.nanglangeats.domain.image.util.S3Util;
import com.sparta.nanglangeats.domain.image.service.dto.ImageResponse;
import com.sparta.nanglangeats.domain.product.controller.dto.request.ProductRequest;
import com.sparta.nanglangeats.domain.product.controller.dto.response.ProductDetailResponse;
import com.sparta.nanglangeats.domain.product.controller.dto.response.ProductListResponse;
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

	private static final int PAGE_SIZE = 10;
	private final StoreRepository storeRepository;
	private final ProductRepository productRepository;
	private final ImageService imageService;
	private final ImageRepository imageRepository;
	private final S3Util s3Util;

	@Transactional
	public ProductResponse createProduct(ProductRequest request, User user) {
		Store store = findStoreByUuid(request.getStoreUuid());
		validateUser(store, user);

		ImageResponse thumbnailResponse = null;

		if (request.getThumbnail() != null) {
			thumbnailResponse = s3Util.uploadFile(request.getThumbnail(), "product-thumbnails");
		}

		Product product = Product.builder()
			.store(store)
			.name(request.getName())
			.description(request.getDescription())
			.price(request.getPrice())
			.thumbnailName(thumbnailResponse != null ? thumbnailResponse.getFileName() : null)
			.thumbnailUrl(thumbnailResponse != null ? thumbnailResponse.getUrl() : null)
			.build();

		productRepository.save(product);

		if (!request.getImages().isEmpty()) {
			imageService.uploadAllImages(request.getImages(), ImageCategory.PRODUCT_IMAGE, product.getId());
		}

		return ProductResponse.builder().productUuid(product.getUuid()).build();
	}

	@Transactional
	public ProductResponse updateProduct(String uuid, ProductRequest request, User user) {
		Product product = findProductByUuid(uuid);
		validateUser(product.getStore(), user);

		ImageResponse thumbnailResponse = null;
		if (request.getThumbnail() != null) {
			if (product.getThumbnailName() != null)
				s3Util.deleteFile(product.getThumbnailName());
			thumbnailResponse = s3Util.uploadFile(request.getThumbnail(), "product-thumbnails");
		}

		product.update(request, thumbnailResponse);

		imageService.hardDeleteAllImages(ImageCategory.PRODUCT_IMAGE, product.getId());
		imageService.uploadAllImages(request.getImages(), ImageCategory.PRODUCT_IMAGE, product.getId());

		return ProductResponse.builder().productUuid(product.getUuid()).build();
	}

	@Transactional
	public void deleteProduct(String uuid, User user) {
		Product product = findProductByUuid(uuid);
		validateUser(product.getStore(), user);

		product.delete(user.getUsername());

		imageService.softDeleteAllImages(ImageCategory.PRODUCT_IMAGE, product.getId(), user.getUsername());
	}

	public ProductDetailResponse getProductDetail(String uuid) {
		Product product = findProductByUuid(uuid);
		validateProduct(product);

		List<String> imageUrls = imageRepository.findUrlsByImageCategoryAndContentId(ImageCategory.PRODUCT_IMAGE,
			product.getId());
		return ProductDetailResponse.builder().product(product).imageUrls(imageUrls).build();
	}

	public List<ProductListResponse> getProductsByStoresList(String storeUuid) {
		Store store = findStoreByUuid(storeUuid);

		List<Product> products = productRepository.findByStoreIdAndIsPublicTrue(store.getId());

		return products.stream().map(product -> ProductListResponse.builder().product(product).build()).toList();
	}

	@Transactional
	public ProductResponse updateProductVisibility(String productUuid, User user) {
		Product product = findProductByUuid(productUuid);
		validateUser(product.getStore(), user);

		product.toggleVisibility();

		return ProductResponse.builder().productUuid(product.getUuid()).build();
	}

	public Page<ProductListResponse> searchProduct(String keyword, int page){
		Sort sort = Sort.by(Sort.Direction.ASC, "name");
		Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

		Page<Product> products = productRepository.searchProductByKeyword(keyword, pageable);

		return products.map(product -> ProductListResponse.builder().product(product).build());
	}

	/* UTIL */
	private void validateUser(Store store, User user) {
		if (user.getRole() == UserRole.OWNER && !store.getOwner().equals(user))
			throw new CustomException(ErrorCode.ACCESS_DENIED);
	}

	private void validateProduct(Product product) {
		if (!product.getIsPublic())
			throw new CustomException(ErrorCode.PRODUCT_NOT_PUBLIC);
		if (!product.getIsActive())
			throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
	}

	private Product findProductByUuid(String uuid) {
		return productRepository.findByUuid(uuid).orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
	}

	private Store findStoreByUuid(String uuid) {
		return storeRepository.findByUuid(uuid).orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
	}
}
