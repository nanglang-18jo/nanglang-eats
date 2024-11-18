package com.sparta.nanglangeats.domain.ai.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiResponse {
	private String statusCode;
	private String msg;
	private String data;
}