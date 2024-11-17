package com.sparta.nanglangeats.domain.ai.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.sparta.nanglangeats.global.config.ai.TextGenerationServiceClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {

	private final AiRepository aiDataRepository;
	private final UserRepository userRepository;
	private final TextGenerationServiceClient textGenerationServiceClient;

	@Transactional
	public CommonResponse<AiResponse> createMenuDescription(AiRequest request, User user) {
		// 권한 확인
		if (!(user.getRole() == UserRole.OWNER)) {
			throw new CustomException(ErrorCode.ACCESS_DENIED);
		}

		// Google AI API 호출하여 응답을 받는 로직
		String question = request.getQuestion();
		String aiAnswer = callGoogleAiApi(question);

		// DB 저장
		AiData aiData = new AiData(request.getUser(), request.getProductName(), aiAnswer);
		aiDataRepository.save(aiData);

		// 응답 생성
		AiResponse response = AiResponse.builder()
			.statusCode("success")
			.msg("메뉴 설명 생성 완료")
			.data(aiAnswer)
			.build();

		return CommonResponse.<AiResponse>builder()
			.statusCode(200)
			.msg("OK")
			.data(response)
			.build();
	}

	private String callGoogleAiApi(String question) {
		// 1. Google AI API 호출 요청 설정
		TextGenerationRequest request = TextGenerationRequest.newBuilder()
			.setModel("text-davinci-003") // 사용할 Google AI 모델 (예: text-davinci-003)
			.setPrompt(question) // 질문을 프롬프트로 설정
			.setMaxTokens(100) // 응답 최대 토큰 수 (조정 가능)
			.setTemperature(0.7) // 생성 결과의 창의성 수준 (0~1 사이, 0에 가까울수록 덜 창의적)
			.build();

		// 2. Google AI API 호출
		TextGenerationResponse response = textGenerationServiceClient.generateText(request);

		// 3. 응답 처리
		String aiAnswer = response.getGeneratedText();
		return aiAnswer;
	}
}