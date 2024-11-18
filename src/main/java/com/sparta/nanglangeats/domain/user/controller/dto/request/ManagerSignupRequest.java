package com.sparta.nanglangeats.domain.user.controller.dto.request;

import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.domain.user.service.dto.request.ManagerSignupServiceRequest;
import com.sparta.nanglangeats.global.common.annotation.Email;
import com.sparta.nanglangeats.global.common.annotation.Password;
import com.sparta.nanglangeats.global.common.annotation.Username;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ManagerSignupRequest {

	@Username
	private String username;
	@Password
	private String password;
	@NotBlank(message = "최소 1자 이상의 닉네임을 반드시 입력해 주세요.")
	private String nickname;
	@Email
	private String email;

	public ManagerSignupServiceRequest toServiceRequest() {
		return ManagerSignupServiceRequest.builder()
			.username(username)
			.password(password)
			.nickname(nickname)
			.email(email)
			.role(UserRole.MANAGER)
			.build();
	}
}
