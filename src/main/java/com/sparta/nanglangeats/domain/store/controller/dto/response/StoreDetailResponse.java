package com.sparta.nanglangeats.domain.store.controller.dto.response;

import java.time.LocalTime;

import com.sparta.nanglangeats.domain.store.entity.Category;
import com.sparta.nanglangeats.domain.store.entity.Store;

import lombok.Builder;
import lombok.Getter;

@Getter
public class StoreDetailResponse {
	private final String category;
	private final String owner;
	private final String name;
	private final String openTime;
	private final String closeTime;
	private final String address;
	private final String phoneNumber;
	private final Float rating;
	private final Integer reviewCount;

	@Builder
	public StoreDetailResponse(Store store){
		this.category = store.getCategory().getName();
		this.owner=store.getOwner().getUsername();
		this.name = store.getName();
		this.openTime = store.getOpenTime().toString();
		this.closeTime = store.getCloseTime().toString();
		this.address=store.getCommonAddress().getAddress() + " " + store.getAddressDetail();
		this.phoneNumber=store.getPhoneNumber();
		this.rating=store.getRating();
		this.reviewCount=store.getReviewCount();
	}
}
