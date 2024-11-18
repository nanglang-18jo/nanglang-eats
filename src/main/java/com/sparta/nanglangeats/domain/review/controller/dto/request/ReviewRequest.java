package com.sparta.nanglangeats.domain.review.controller.dto.request;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class ReviewRequest {

	@NotBlank(message = "리뷰를 등록할 주문의 UUID를 입력해주세요.")
	private String orderUuid;

	private String content;

	private List<MultipartFile> images;

	@NotNull(message = "별점은 필수 항목입니다.")
	private Integer rating;
}