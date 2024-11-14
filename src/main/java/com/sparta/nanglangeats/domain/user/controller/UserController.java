package com.sparta.nanglangeats.domain.user.controller;

import static com.sparta.nanglangeats.global.common.util.ControllerUtil.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.user.controller.dto.request.UserSignupRequest;
import com.sparta.nanglangeats.domain.user.controller.dto.request.UserUpdateRequest;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.domain.user.service.UserService;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/api/users/signup")
	public ResponseEntity<CommonResponse<?>> createUser(@Valid @RequestBody UserSignupRequest request) {
		return getResponseEntity(CREATED, userService.createUser(request.toServiceRequest()), "유저 회원 가입 성공");
	}

	@PutMapping("/api/users")
	public ResponseEntity<CommonResponse<?>> updateUser(@AuthenticationPrincipal User user, @Valid @RequestBody UserUpdateRequest request) {
		return getResponseEntity(OK, userService.updateUser(user, request), "유저 회원 정보 수정 완료");
	}
}
