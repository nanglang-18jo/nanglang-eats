package com.sparta.nanglangeats.domain.store.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.store.controller.dto.request.StoreRequest;
import com.sparta.nanglangeats.domain.store.service.StoreService;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static com.sparta.nanglangeats.global.common.util.ControllerUtil.getResponseEntity;
import static com.sparta.nanglangeats.global.common.util.ControllerUtil.getOkResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stores")
public class StoreController {
	private final StoreService storeService;

	@PostMapping
	@PreAuthorize("hasAnyRole('MANAGER')")
	public ResponseEntity<CommonResponse<?>> createStore(
		@ModelAttribute @Valid StoreRequest request) {
		return getResponseEntity(HttpStatus.CREATED, storeService.createStore(request), "가게 등록 완료");
	}

	@PutMapping("/{storeId}")
	@PreAuthorize("hasAnyRole('OWNER')")
	public ResponseEntity<CommonResponse<?>> updateStore(
		@PathVariable Long storeId,
		@ModelAttribute StoreRequest request,
		@AuthenticationPrincipal User user){
		return getOkResponseEntity(storeService.updateStore(storeId, request, user), "가게 수정 완료");
	}

	@DeleteMapping("/{storeId}")
	@PreAuthorize("hasAnyRole('OWNER')")
	public ResponseEntity<CommonResponse<?>> deleteStore(
		@PathVariable Long storeId,
		@AuthenticationPrincipal User user){
		storeService.deleteStore(storeId, user);
		return getResponseEntity(HttpStatus.NO_CONTENT, null, "가게 삭제 완료");
	}
}
