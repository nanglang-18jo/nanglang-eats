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
	public String getQuestion() {
		return "어떤 메뉴의 설명을 원하십니까? : " + productName;
	}
}