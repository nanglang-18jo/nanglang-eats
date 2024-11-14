package com.sparta.nanglangeats.domain.image.service.dto;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.nanglangeats.domain.image.entity.Image;
import com.sparta.nanglangeats.domain.image.enums.ImageCategory;
import com.sparta.nanglangeats.domain.image.repository.ImageRepository;
import com.sparta.nanglangeats.domain.image.util.S3Util;
import com.sparta.nanglangeats.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
	private final S3Util s3Util;
	private final ImageRepository imageRepository;

	public ImageResponse uploadImage(MultipartFile image, String dirName) {
		return s3Util.uploadFile(image, dirName);
	}

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
			Image storeImage = new Image(uploadImage(image, dirName), contentId, category);
			imageRepository.save(storeImage);
		}
	}

	@Transactional
	public void deleteAllImages(ImageCategory category, Long contentId) {
		List<Image> images = imageRepository.findAllByImageCategoryAndAndContentId(category, contentId);
		imageRepository.deleteAll(images);
	}
}
