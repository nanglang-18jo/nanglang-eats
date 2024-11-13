package com.sparta.nanglangeats.domain.user.controller.dto.request;

import static com.sparta.nanglangeats.global.common.exception.ErrorCode.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sparta.nanglangeats.domain.user.enums.UserRole;
import com.sparta.nanglangeats.domain.user.service.dto.request.UserSignupServiceRequest;
import com.sparta.nanglangeats.global.common.exception.CustomException;

class UserSignupRequestTest {

	@Test
	@DisplayName("toServiceRequest(): Client로부터 전달받은 값들을 통해 UserSignupServiceRequest 객체를 생성한다.")
	void toService_success() {
		// given
		final String username = "tester";
		final String password = "password";
		final String nickname = "tester";
		final String email = "test@gmail.com";
		final UserRole role = UserRole.CUSTOMER;

		// when
		final UserSignupServiceRequest request = new UserSignupRequest(username, password, nickname, email, role).toServiceRequest();

		// then
		assertThat(request.getUsername()).isEqualTo(username);
		assertThat(request.getPassword()).isEqualTo(password);
		assertThat(request.getNickname()).isEqualTo(nickname);
		assertThat(request.getEmail()).isEqualTo(email);
		assertThat(request.getRole()).isEqualTo(role);
	}

	@Test
	@DisplayName("toServiceRequest(): CUSTOMER, OWNER 외에 권한을 요청하면 UserSignupServiceRequest 객체 생성에 실패한다.")
	void toService_role_validation() {
		// given
		final String username = "tester";
		final String password = "password";
		final String nickname = "tester";
		final String email = "test@gmail.com";
		final UserRole role = UserRole.MASTER;

		// expected
		assertThatThrownBy(() -> new UserSignupRequest(username, password, nickname, email, role).toServiceRequest())
			.isInstanceOf(CustomException.class)
			.hasMessage(INVALID_ROLE_REQUEST.getMessage());
	}
}
