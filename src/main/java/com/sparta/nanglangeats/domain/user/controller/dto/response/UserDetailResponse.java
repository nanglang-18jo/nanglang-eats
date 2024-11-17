package com.sparta.nanglangeats.domain.user.controller.dto.response;

import com.sparta.nanglangeats.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {

	private String nickname;
	// todo: 평균 점수, 가게별 리뷰 정보 추가

	public static UserDetailResponse from(final User user) {
		return new UserDetailResponse(user.getNickname());
	}
}
