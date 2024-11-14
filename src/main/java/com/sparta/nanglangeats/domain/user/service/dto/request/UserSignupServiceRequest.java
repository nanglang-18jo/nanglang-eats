package com.sparta.nanglangeats.domain.user.service.dto.request;

import com.sparta.nanglangeats.domain.user.enums.UserRole;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignupServiceRequest {

	private String username;
	private String password;
	private String nickname;
	private String email;
	private UserRole role;

	@Builder
	public UserSignupServiceRequest(String username, String password, String nickname, String email, UserRole role) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.email = email;
		this.role = role;
	}
}
