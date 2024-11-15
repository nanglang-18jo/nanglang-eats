package com.sparta.nanglangeats.domain.ai.dto.request;

import com.sparta.nanglangeats.domain.user.entity.User;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiRequest {
	private User user;
	private String productName;
	@NotNull(message = "가게 주인 ID를 입력해주세요.")
	private Long ownerId;
}