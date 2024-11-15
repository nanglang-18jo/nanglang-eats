package com.sparta.nanglangeats.domain.ai.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.ai.dto.request.AiRequest;
import com.sparta.nanglangeats.domain.ai.dto.response.AiResponse;
import com.sparta.nanglangeats.domain.ai.entity.AiData;
import com.sparta.nanglangeats.domain.ai.repository.AiRepository;
import com.sparta.nanglangeats.domain.store.repository.StoreRepository;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.domain.user.repository.UserRepository;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiService {

	private final AiRepository aiDataRepository;
	private	final UserRepository userRepository;

	@Transactional
	public CommonResponse<AiResponse> createMenuDescription(AiRequest request, User user) {
		// 권한 확인
		if (!(user.getRole() == UserRole.OWNER))
			throw new CustomException(ErrorCode.ACCESS_DENIED);

		User owner = validateUser(request.getOwnerId());

		// Google AI API 호출하여 응답을 받는 로직을 여기에 추가합니다.
		String aiAnswer = "맛있는 " + request.getProductName() + "입니다."; // 실제 응답 예시

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

	/* UTIL */
	private User validateUser(Long userId) {
		User user=userRepository.findById(userId).orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
		if(!user.getRole().equals(UserRole.OWNER)) throw new CustomException(ErrorCode.USER_ROLE_NOT_OWNER);
		return user;
	}
}