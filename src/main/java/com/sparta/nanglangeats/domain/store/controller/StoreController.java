package com.sparta.nanglangeats.domain.store.controller;

import static com.sparta.nanglangeats.global.common.util.ControllerUtil.*;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.nanglangeats.domain.store.controller.dto.request.StoreRequest;
import com.sparta.nanglangeats.domain.store.service.StoreService;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.dto.CommonResponse;
import com.sparta.nanglangeats.global.common.exception.CustomException;
import com.sparta.nanglangeats.global.common.exception.ErrorCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

	@PutMapping("/{uuid}")
	@PreAuthorize("hasAnyRole('OWNER')")
	public ResponseEntity<CommonResponse<?>> updateStore(
		@PathVariable String uuid,
		@ModelAttribute StoreRequest request,
		@AuthenticationPrincipal User user) {
		return getOkResponseEntity(storeService.updateStore(uuid, request, user), "가게 수정 완료");
	}

	@DeleteMapping("/{uuid}")
	@PreAuthorize("hasAnyRole('OWNER')")
	public ResponseEntity<CommonResponse<?>> deleteStore(
		@PathVariable String uuid,
		@AuthenticationPrincipal User user) {
		storeService.deleteStore(uuid, user);
		return getResponseEntity(HttpStatus.NO_CONTENT, null, "가게 삭제 완료");
	}

	@GetMapping("/{uuid}")
	public ResponseEntity<CommonResponse<?>> getStoreDetail(
		@PathVariable String uuid) {
		return getOkResponseEntity(storeService.getStoreDetail(uuid), "가게 상세 조회 완료");
	}

	@GetMapping
	public ResponseEntity<CommonResponse<?>> getStoresList(
		@RequestParam Long categoryId,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "name") String sortBy) {
		List<String> validSortByFields = Arrays.asList("name", "reviewCount", "rating");
		if (!validSortByFields.contains(sortBy))
			throw new CustomException(ErrorCode.INVALID_SORTBY_PARAMETER);

		String direction = "desc";
		if (sortBy.equals("name"))
			direction = "asc";

		return getOkResponseEntity(storeService.getStoresList(categoryId, page, sortBy, direction),
			"가게 목록 조회 완료");
	}

	@GetMapping("/search")
	public ResponseEntity<CommonResponse<?>> searchStore(
		@RequestParam String keyword,
		@RequestParam(defaultValue = "0") int page){
		return getOkResponseEntity(storeService.searchStore(keyword, page), "가게 검색 완료");
	}
}
