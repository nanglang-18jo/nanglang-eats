package com.sparta.nanglangeats.domain.delivery_address.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressUpdateRequest {

	@NotBlank(message = "배송지 주소를 입력해 주세요.")
	private String address;
	private String addressDetail;
	private String alias;
	private Boolean isRecentDelivery;
}
