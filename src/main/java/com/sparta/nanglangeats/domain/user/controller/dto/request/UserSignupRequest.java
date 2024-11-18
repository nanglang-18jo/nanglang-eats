package com.sparta.nanglangeats.domain.user.controller.dto.request;

import static com.sparta.nanglangeats.global.common.exception.ErrorCode.*;

import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.domain.user.service.dto.request.UserSignupServiceRequest;
import com.sparta.nanglangeats.global.common.annotation.Email;
import com.sparta.nanglangeats.global.common.annotation.Password;
import com.sparta.nanglangeats.global.common.annotation.Username;
import com.sparta.nanglangeats.global.common.exception.CustomException;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupRequest {

	@Username
	private String username;
	@Password
	private String password;
	@NotBlank(message = "최소 1자 이상의 닉네임을 반드시 입력해 주세요.")
	private String nickname;
	@Email
	private String email;
	@NotNull(message = "유저의 권한을 선택해 주세요.")
	private UserRole role;

	public UserSignupServiceRequest toServiceRequest() {
		validateRole();
		return UserSignupServiceRequest.builder()
			.username(username)
			.password(password)
			.nickname(nickname)
			.email(email)
			.role(role)
			.build();
	}

	private void validateRole() {
		if (role == UserRole.MANAGER || role == UserRole.MASTER) {
			throw new CustomException(INVALID_ROLE_REQUEST);
		}
	}
}
