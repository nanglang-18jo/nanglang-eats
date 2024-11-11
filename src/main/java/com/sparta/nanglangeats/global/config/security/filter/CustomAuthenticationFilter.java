package com.sparta.nanglangeats.global.config.security.filter;

import java.io.IOException;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.nanglangeats.domain.user.controller.dto.request.UserSignupRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public CustomAuthenticationFilter() {
		super(new AntPathRequestMatcher("/api/auth/login", "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		if (!HttpMethod.POST.name().equals(request.getMethod())) {
			throw new AuthenticationServiceException("Authentication method not supported");
		}

		UserSignupRequest signupRequest = mapper.readValue(request.getReader(), UserSignupRequest.class);

		if (!StringUtils.hasText(signupRequest.getUsername()) || !StringUtils.hasText(signupRequest.getPassword())) {
			throw new AuthenticationServiceException("Username or Password not provided");
		}

		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(signupRequest.getUsername(), signupRequest.getPassword());

		return this.getAuthenticationManager().authenticate(token);
	}
}
