package com.sparta.nanglangeats.domain.image.service.dto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.nanglangeats.domain.image.util.S3Util;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {
	private final S3Util s3Util;

	public ImageResponse uploadImage(MultipartFile image, String dirName) {
		return s3Util.uploadFile(image, dirName);
	}
}
