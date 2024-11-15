package com.sparta.nanglangeats.domain.ai.dto.request;

import com.sparta.nanglangeats.domain.store.entity.Store;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiRequest {
	private Store store;
	private String productName;
}