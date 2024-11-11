package com.sparta.nanglangeats.global.config.security.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final String ACCESS_DENIED_MESSAGE = "접근 권한이 존재하지 않습니다.";

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter()
			.write(mapper.writeValueAsString(ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(CommonResponse.builder()
					.statusCode(HttpStatus.FORBIDDEN.value())
					.msg(ACCESS_DENIED_MESSAGE)
					.build())));
	}
}
