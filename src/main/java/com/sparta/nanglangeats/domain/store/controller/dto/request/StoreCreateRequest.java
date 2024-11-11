package com.sparta.nanglangeats.domain.store.controller.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreCreateRequest {

	@NotNull(message = "카테고리 정보를 입력해주세요.")
	private Long categoryId;

	@NotBlank(message = "상호명을 입력해주세요.")
	private String name;

	@NotNull(message = "가게 운영 시작 시간을 입력해주세요.")
	private LocalDateTime openTime;

	@NotNull(message = "가게 운영 종료 시간을 입력해주세요.")
	private LocalDateTime closeTime;

	@NotBlank(message = "가게 주소를 입력해주세요.")
	private String address;

	private String addressDetail;
	private String phoneNumber;

	private List<MultipartFile> images;
}
