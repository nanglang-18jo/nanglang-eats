package com.sparta.nanglangeats.domain.store.controller.dto.request;

import java.time.LocalTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreRequest {

	@NotNull(message = "카테고리 정보를 입력해주세요.")
	private Long categoryId;

	@NotBlank(message = "상호명을 입력해주세요.")
	private String name;

	@NotNull(message = "가게 주인 ID를 입력해주세요.")
	private Long ownerId;

	@NotNull(message = "가게 운영 시작 시간을 입력해주세요.")
	private LocalTime openTime;

	@NotNull(message = "가게 운영 종료 시간을 입력해주세요.")
	private LocalTime closeTime;

	@NotBlank(message = "가게 주소를 입력해주세요.")
	private String address;

	private String addressDetail;
	
	private String phoneNumber;

	private MultipartFile thumbnail;

	private List<MultipartFile> images;
}
