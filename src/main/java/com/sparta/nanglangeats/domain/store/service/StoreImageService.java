package com.sparta.nanglangeats.domain.store.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.nanglangeats.domain.image.service.dto.ImageResponse;
import com.sparta.nanglangeats.domain.image.util.S3Util;
import com.sparta.nanglangeats.domain.store.entity.Store;
import com.sparta.nanglangeats.domain.store.entity.StoreImage;
import com.sparta.nanglangeats.domain.store.repository.StoreImageRepository;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreImageService {

	private final S3Util s3Util;
	private final StoreImageRepository storeImageRepository;

	public ImageResponse uploadImage(MultipartFile image) {
		return s3Util.uploadFile(image, "store-images");
	}

	public void saveStoreImages(Store store, List<MultipartFile> images) {
		if (images.size() > 5) {
			throw new CustomException(ErrorCode.IMAGE_COUNT_EXCEEDED);
		}

		List<StoreImage> imageList = images.stream()
			.map(img -> new StoreImage(store, uploadImage(img)))
			.toList();

		storeImageRepository.saveAll(imageList);
	}
}
