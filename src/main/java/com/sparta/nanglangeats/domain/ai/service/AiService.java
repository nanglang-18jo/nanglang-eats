package com.sparta.nanglangeats.domain.ai.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.ai.dto.request.AiRequest;
import com.sparta.nanglangeats.domain.ai.dto.response.AiResponse;
import com.sparta.nanglangeats.domain.ai.entity.AiData;
import com.sparta.nanglangeats.domain.ai.repository.AiRepository;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.repository.UserRepository;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {

	private final AiRepository aiDataRepository;
	private final UserRepository userRepository;
	private final GoogleApi googleApi; // Inject Google AI client

	@Transactional
	public CommonResponse<AiResponse> createMenuDescription(AiRequest request, User user) {
		// prompt 생성
		String prompt = "설명이 필요한 음식을 50자 이내로 작성해주세요: " + request.getProductName();

		// Google AI API 호출
		String aiResponse = googleApi.generateText(prompt);

		// 데이터 저장
		AiData aiData = AiData.builder()
			.user(user)
			.question(prompt)
			.answer(aiResponse)
			.build();
		aiDataRepository.save(aiData);

		// 응답 반환
		return CommonResponse.<AiResponse>builder()
			.statusCode(HttpStatus.CREATED.value())
			.msg("메뉴 설명 생성 완료")
			.data(AiResponse.builder()
				.data(aiResponse)
				.build())
			.build();
	}
}