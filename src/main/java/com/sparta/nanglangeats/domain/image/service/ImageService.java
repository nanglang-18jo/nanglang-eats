package com.sparta.nanglangeats.domain.image.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.nanglangeats.domain.image.entity.Image;
import com.sparta.nanglangeats.domain.image.enums.ImageCategory;
import com.sparta.nanglangeats.domain.image.repository.ImageRepository;
import com.sparta.nanglangeats.domain.image.service.dto.ImageResponse;
import com.sparta.nanglangeats.domain.image.util.S3Util;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
	private final S3Util s3Util;
	private final ImageRepository imageRepository;

	@Transactional
	public List<String> uploadAllImages(List<MultipartFile> images, ImageCategory category, Long contentId) {
		String dirName = "";
		if (category == ImageCategory.STORE_IMAGE)
			dirName = "store-images";
		else if (category == ImageCategory.PRODUCT_IMAGE)
			dirName = "product-images";
		else if (category == ImageCategory.REVIEW_IMAGE)
			dirName = "review-images";

		List<String> imageUrls = new ArrayList<>();

		for (MultipartFile image : images) {
			ImageResponse imageResponse = s3Util.uploadFile(image, dirName);
			Image storeImage = new Image(imageResponse, contentId, category);
			imageRepository.save(storeImage);
			imageUrls.add(imageResponse.getUrl());
		}
		return imageUrls;
	}

	@Transactional
	public void hardDeleteAllImages(ImageCategory category, Long contentId) {
		List<Image> images = imageRepository.findAllByImageCategoryAndContentId(category, contentId);
		imageRepository.deleteAll(images);
		images.forEach(image -> s3Util.deleteFile(image.getFileName()));
	}

	@Transactional
	public void softDeleteAllImages(ImageCategory category, Long contentId, String deleteBy) {
		List<Image> images = imageRepository.findAllByImageCategoryAndContentId(category, contentId);
		images.forEach(image -> image.delete(deleteBy));
	}

}
