package com.sparta.nanglangeats.domain.user.controller;

import static com.sparta.nanglangeats.global.common.util.ControllerUtil.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.user.controller.dto.request.ManagerSignupRequest;
import com.sparta.nanglangeats.domain.user.controller.dto.request.UserSignupRequest;
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
		return getResponseEntity(CREATED, userService.createUser(request.toServiceRequest()), "고객 회원 가입 성공");
	}

	@PreAuthorize("hasRole('MASTER')")
	@PostMapping("/api/admin/managers")
	public ResponseEntity<CommonResponse<?>> createManager(@Valid @RequestBody ManagerSignupRequest request) {
		return getResponseEntity(CREATED, userService.createManager(request.toServiceRequest()), "매니저 회원 가입 성공");
	}
}
