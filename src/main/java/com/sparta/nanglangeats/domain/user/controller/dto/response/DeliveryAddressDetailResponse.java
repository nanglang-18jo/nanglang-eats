package com.sparta.nanglangeats.domain.user.controller.dto.response;

import java.time.LocalDateTime;

import com.sparta.nanglangeats.domain.user.entity.DeliveryAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressDetailResponse {

	private Long deliveryAddressId;
	private String commonAddress;
	private String addressDetail;
	private String alias;
	private boolean isRecentDelivery;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static DeliveryAddressDetailResponse from(DeliveryAddress deliveryAddress) {
		return new DeliveryAddressDetailResponse(
			deliveryAddress.getId(),
			deliveryAddress.getCommonAddress().getAddress(),
			deliveryAddress.getAddressDetail(),
			deliveryAddress.getAlias(),
			deliveryAddress.isRecentDelivery(),
			deliveryAddress.getCreatedAt(),
			deliveryAddress.getUpdatedAt());
	}
}
