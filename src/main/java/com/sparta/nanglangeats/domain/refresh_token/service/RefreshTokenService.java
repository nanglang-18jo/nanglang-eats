package com.sparta.nanglangeats.domain.refresh_token.service;

import static com.sparta.nanglangeats.global.common.exception.ErrorCode.*;
import static com.sparta.nanglangeats.global.config.security.jwt.JwtTokenProvider.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.refresh_token.entity.RefreshToken;
import com.sparta.nanglangeats.domain.refresh_token.repository.RefreshTokenRepository;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.config.security.jwt.JwtTokenProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtTokenProvider jwtTokenProvider;

	@Transactional
	public void reissue(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = jwtTokenProvider.getRefreshToken(request);
		setResponse(response, refreshToken);
	}

	public void logout(HttpServletRequest request, HttpServletResponse response) {
		String refreshToken = jwtTokenProvider.getRefreshToken(request);
		deleteRefreshToken(refreshToken);
		deleteCookie(response);
	}

	private void setResponse(HttpServletResponse response, String refreshToken) {
		RefreshToken findRefreshToken = getRefreshToken(refreshToken);

		String newAccessToken = jwtTokenProvider.createAccessToken(findRefreshToken.getEmail(), findRefreshToken.getRole().getAuthority());
		String newRefreshToken = jwtTokenProvider.createRefreshToken();

		findRefreshToken.update(newRefreshToken);

		response.addHeader(AUTHORIZATION_HEADER, newAccessToken);
		jwtTokenProvider.addCookie(response, newRefreshToken);
	}

	@Transactional
	protected void deleteRefreshToken(String refreshToken) {
		RefreshToken findRefreshToken = getRefreshToken(refreshToken);
		refreshTokenRepository.delete(findRefreshToken);
	}

	private void deleteCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	@Transactional(readOnly = true)
	public RefreshToken getRefreshToken(String refreshToken) {
		return refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new CustomException(INVALID_REFRESH_TOKEN));
	}
}
