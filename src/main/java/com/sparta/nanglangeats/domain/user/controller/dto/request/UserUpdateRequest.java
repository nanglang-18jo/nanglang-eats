package com.sparta.nanglangeats.domain.user.controller.dto.request;

import com.sparta.nanglangeats.global.common.annotation.Email;
import com.sparta.nanglangeats.global.common.annotation.Password;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

	@Password
	private String password;
	@NotBlank
	private String  nickname;
	@Email
	private String email;
	@NotNull
	private Boolean isActive;
}
