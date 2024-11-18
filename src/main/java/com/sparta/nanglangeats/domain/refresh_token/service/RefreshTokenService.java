package com.sparta.nanglangeats.domain.refresh_token.service;

import static com.sparta.nanglangeats.global.common.exception.ErrorCode.*;
import static com.sparta.nanglangeats.global.config.security.jwt.JwtTokenProvider.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.nanglangeats.domain.refresh_token.entity.RefreshToken;
import com.sparta.nanglangeats.domain.refresh_token.repository.RefreshTokenRepository;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.config.security.jwt.JwtTokenProvider;

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

	private void setResponse(HttpServletResponse response, String refreshToken) {
		RefreshToken findRefreshToken = getRefreshToken(refreshToken);

		String newAccessToken = jwtTokenProvider.createAccessToken(findRefreshToken.getEmail(), findRefreshToken.getRole().getAuthority());
		String newRefreshToken = jwtTokenProvider.createRefreshToken();

		findRefreshToken.update(newRefreshToken);

		response.addHeader(AUTHORIZATION_HEADER, newAccessToken);
		jwtTokenProvider.addCookie(response, newRefreshToken);
	}

	@Transactional(readOnly = true)
	public RefreshToken getRefreshToken(String refreshToken) {
		return refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new CustomException(INVALID_REFRESH_TOKEN));
	}
}
