package com.sparta.nanglangeats.domain.store.controller.dto.request;

import java.time.LocalTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreUpdateRequest {
	private Long categoryId;
	private String name;
	private Long ownerId;
	private LocalTime openTime;
	private LocalTime closeTime;
	private String address;
	private String addressDetail;
	private String phoneNumber;
	private List<Long> deleteImageIds;
	private List<MultipartFile> newImages;
}
