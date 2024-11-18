package com.sparta.nanglangeats.domain.user.controller.dto.response;

import com.sparta.nanglangeats.domain.user.entity.DeliveryAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressListResponse {

	private Long deliveryAddressId;
	private String commonAddress;
	private String addressDetail;
	private String alias;
	private boolean isRecentDelivery;

	public static DeliveryAddressListResponse from(DeliveryAddress deliveryAddress) {
		return new DeliveryAddressListResponse(
			deliveryAddress.getId(),
			deliveryAddress.getCommonAddress().getAddress(),
			deliveryAddress.getAddressDetail(),
			deliveryAddress.getAlias(),
			deliveryAddress.isRecentDelivery());
	}
}
