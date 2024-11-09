package com.sparta.nanglangeats.global.config.security.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;
import com.sparta.nanglangeats.global.common.exception.CustomException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final String LOGIN_FAILURE = "로그인에 실패하셨습니다.";

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException, CustomException {
		ObjectMapper mapper = new ObjectMapper();

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter()
			.write(mapper.writeValueAsString(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(CommonResponse.builder()
					.statusCode(HttpStatus.UNAUTHORIZED.value())
					.msg(LOGIN_FAILURE)
					.build())));
	}
}
