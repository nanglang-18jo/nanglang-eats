package com.sparta.nanglangeats.domain.review.controller.dto.request;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
	private UUID userId;
	private UUID storeId;
	private UUID orderId;
	private String content;
	private List<MultipartFile> images;
	private int rating;
}