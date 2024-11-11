package com.sparta.nanglangeats.domain.store.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.store.controller.dto.request.StoreCreateRequest;
import com.sparta.nanglangeats.domain.store.service.StoreService;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static com.sparta.nanglangeats.global.common.util.ControllerUtil.getResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {
	private final StoreService storeService;

	@PostMapping
	public ResponseEntity<CommonResponse<?>> insertStore(
		@ModelAttribute @Valid StoreCreateRequest request,
		@AuthenticationPrincipal User user) {
		return getResponseEntity(storeService.insertStore(request, user), "가게 등록 완료");
	}
}
