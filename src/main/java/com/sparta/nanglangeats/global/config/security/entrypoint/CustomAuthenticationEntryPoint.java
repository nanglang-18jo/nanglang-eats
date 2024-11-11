package com.sparta.nanglangeats.global.config.security.entrypoint;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final String UNAUTHORIZED_MESSAGE = "인증되지 않은 사용자입니다.";

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter()
			.write(mapper.writeValueAsString(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(CommonResponse.builder()
					.statusCode(HttpStatus.UNAUTHORIZED.value())
					.msg(UNAUTHORIZED_MESSAGE)
					.build())));
	}
}
