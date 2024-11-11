package com.sparta.nanglangeats.global.config.security.handler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.util.ControllerUtil;
import com.sparta.nanglangeats.global.config.security.jwt.JwtTokenProvider;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private static final String LOGIN_SUCCESS = "로그인에 성공하셨습니다.";
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		User user = (User)authentication.getPrincipal();
		String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getRole().getAuthority());
		response.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, accessToken);

		ObjectMapper mapper = new ObjectMapper();
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(mapper.writeValueAsString(ControllerUtil.getResponseEntity(LOGIN_SUCCESS)));
	}
}
