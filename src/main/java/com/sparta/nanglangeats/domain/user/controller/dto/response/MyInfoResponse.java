package com.sparta.nanglangeats.domain.user.controller.dto.response;

import com.sparta.nanglangeats.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MyInfoResponse {

	private String username;
	private String nickname;
	private String email;

	public static MyInfoResponse from(User user) {
		return new MyInfoResponse(user.getUsername(), user.getNickname(), user.getEmail());
	}
}
