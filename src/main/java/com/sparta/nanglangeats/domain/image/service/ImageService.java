package com.sparta.nanglangeats.domain.image.service;

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
	public void uploadAllImages(List<MultipartFile> images, ImageCategory category, Long contentId) {
		String dirName = "";
		if (category == ImageCategory.STORE_IMAGE)
			dirName = "store-images";
		else if (category == ImageCategory.PRODUCT_IMAGE)
			dirName = "product-images";
		else if (category == ImageCategory.REVIEW_IMAGE)
			dirName = "review-images";

		for (MultipartFile image : images) {
			Image storeImage = new Image(s3Util.uploadFile(image, dirName), contentId, category);
			imageRepository.save(storeImage);
		}
	}

	@Transactional
	public ImageResponse changeImage(String fileName, String dirName, MultipartFile image) {
		s3Util.deleteFile(fileName);
		return uploadImage(image, dirName);
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
