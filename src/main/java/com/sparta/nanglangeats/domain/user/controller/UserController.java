package com.sparta.nanglangeats.domain.user.controller;

import static com.sparta.nanglangeats.global.common.util.ControllerUtil.*;
import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.user.controller.dto.request.ManagerSignupRequest;
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
		return getResponseEntity(CREATED, userService.createUser(request.toServiceRequest()), "회원 가입 성공");
	}

	@PreAuthorize("hasRole('MASTER')")
	@PostMapping("/api/admin/managers")
	public ResponseEntity<CommonResponse<?>> createManager(@Valid @RequestBody ManagerSignupRequest request) {
		return getResponseEntity(CREATED, userService.createManager(request.toServiceRequest()), "매니저 등록 성공");
	}

	@PutMapping("/api/users/me")
	public ResponseEntity<CommonResponse<?>> updateMyInfo(@AuthenticationPrincipal User user, @Valid @RequestBody UserUpdateRequest request) {
		return getOkResponseEntity(userService.updateMyInfo(user, request), "내 정보 수정 성공");
	}
	
	@DeleteMapping("/api/users/me")
	public ResponseEntity<CommonResponse<?>> deleteMyAccount(@AuthenticationPrincipal User user) {
		return getResponseEntity(NO_CONTENT, userService.deleteMyAccount(user), "회원 탈퇴 성공");
	}

	@GetMapping("/api/users/me")
	public ResponseEntity<CommonResponse<?>> getMyInfo(@AuthenticationPrincipal User user) {
		return getOkResponseEntity(userService.getMyInfo(user), "내 정보 조회 성공");
	}

	@GetMapping("/api/users")
	public ResponseEntity<CommonResponse<?>> getUserDetailByNickname(@RequestParam String nickname) {
		return getOkResponseEntity(userService.getUserDetailByNickname(nickname), "유저 상세 정보 조회 성공");
	}
}
