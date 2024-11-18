package com.sparta.nanglangeats.domain.ai.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.sparta.nanglangeats.domain.ai.dto.request.AiRequest;
import com.sparta.nanglangeats.domain.ai.dto.response.AiResponse;
import com.sparta.nanglangeats.domain.ai.entity.AiData;
import com.sparta.nanglangeats.domain.ai.repository.AiRepository;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.domain.user.repository.UserRepository;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.common.exception.ErrorCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {

	private final AiRepository aiDataRepository;
	private final UserRepository userRepository;
	private final GoogleAiClient googleAiClient; // Inject Google AI client

	@Transactional
	public CommonResponse<AiResponse> createMenuDescription(AiRequest request, User user) {
		// Authorization check
		if (!(user.getRole() == UserRole.OWNER))
			throw new CustomException(ErrorCode.ACCESS_DENIED);

		// Generate prompt
		String prompt = "설명이 필요한 음식을 작성해주세요: " + request.getProductName();

		// Call Google AI API
		String aiResponse = googleAiClient.generateText(prompt);

		// Persist data
		AiData aiData = AiData.builder()
			.user(user)
			.question(prompt)
			.answer(aiResponse)
			.build();
		aiDataRepository.save(aiData);

		// Return response
		return CommonResponse.<AiResponse>builder()
			.statusCode(HttpStatus.CREATED.value())
			.msg("메뉴 설명 생성 완료")
			.data(AiResponse.builder()
				.data(aiResponse)
				.build())
			.build();
	}
}