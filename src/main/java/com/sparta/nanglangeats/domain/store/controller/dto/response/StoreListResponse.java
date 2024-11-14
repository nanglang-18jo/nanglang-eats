package com.sparta.nanglangeats.domain.store.controller.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreListResponse {
	private final String uuid;
	private final String name;
	private final Float rating;
	private final Integer reviewCount;
	private final List<String> imagesUrl;
}
