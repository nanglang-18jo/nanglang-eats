package com.sparta.nanglangeats.domain.ai.controller;

import static com.sparta.nanglangeats.global.common.util.ControllerUtil.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.ai.dto.request.AiRequest;
import com.sparta.nanglangeats.domain.ai.service.AiService;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai")
@PreAuthorize("hasRole('OWNER')")
public class AiController {
	private final AiService aiService;

	@PostMapping
	public ResponseEntity<CommonResponse<?>> createMenuDescription(
		@ModelAttribute @Valid AiRequest request,
		@AuthenticationPrincipal User user) {
		return getResponseEntity(HttpStatus.CREATED, aiService.createMenuDescription(request, user), "메뉴 설명 완료");
	}
}
