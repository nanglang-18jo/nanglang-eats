package com.sparta.nanglangeats.domain.user.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequest {

	private String username;
	private String password;
	private String nickname;
	private String email;
}
