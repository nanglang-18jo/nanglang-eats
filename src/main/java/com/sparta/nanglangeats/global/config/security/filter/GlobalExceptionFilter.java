package com.sparta.nanglangeats.global.config.security.filter;

import static com.sparta.nanglangeats.global.common.exception.ErrorCode.*;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;
import com.sparta.nanglangeats.global.common.exception.ErrorCode;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class GlobalExceptionFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			setResponse(response, EXPIRED_ACCESS_TOKEN);
		} catch (JwtException | IllegalArgumentException e) {
			setResponse(response, COMMON_SYSTEM_ERROR);
		}
	}

	private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		response.setStatus(errorCode.getHttpStatus().value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter()
			.write(mapper.writeValueAsString(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(CommonResponse.builder()
					.statusCode(HttpStatus.UNAUTHORIZED.value())
					.msg(errorCode.getMessage())
					.build())));
	}
}
