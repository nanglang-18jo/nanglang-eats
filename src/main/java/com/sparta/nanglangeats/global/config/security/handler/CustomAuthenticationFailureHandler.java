package com.sparta.nanglangeats.global.config.security.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;
import com.sparta.nanglangeats.global.common.exception.CustomException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final String LOGIN_FAILURE = "로그인에 실패하셨습니다.";

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,AuthenticationException exception) throws IOException, CustomException {
		ObjectMapper mapper = new ObjectMapper();

		String errorMessage = LOGIN_FAILURE;
		if (exception instanceof BadCredentialsException) {
			errorMessage = exception.getMessage();
		} else if (exception instanceof UsernameNotFoundException) {
			errorMessage = exception.getMessage();
		}

		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter()
			.write(mapper.writeValueAsString(ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(CommonResponse.builder()
					.statusCode(HttpStatus.BAD_REQUEST.value())
					.msg(errorMessage)
					.build())));
	}
}
