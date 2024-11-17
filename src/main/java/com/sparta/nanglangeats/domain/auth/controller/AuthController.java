package com.sparta.nanglangeats.domain.auth.controller;

import static com.sparta.nanglangeats.global.common.util.ControllerUtil.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.refresh_token.service.RefreshTokenService;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	private final RefreshTokenService refreshTokenService;

	@PostMapping("/api/auth/reissue")
	public ResponseEntity<CommonResponse<?>> reissue(HttpServletRequest request, HttpServletResponse response) {
		refreshTokenService.reissue(request, response);
		return getResponseEntity(CREATED, "토큰 재발급 성공");
	}
}
