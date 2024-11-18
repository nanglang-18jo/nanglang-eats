package com.sparta.nanglangeats.domain.order.controller.dto.request;

import java.util.List;

import com.sparta.nanglangeats.domain.order.enums.OrderType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderUpdateRequest {

	@NotBlank(message = "주소는 필수 항목입니다.")
	private String address;

	private String requirement;

	@NotNull(message = "주문 방식(type)은 필수 항목입니다.")
	private OrderType type;

	@NotNull(message = "총 주문 금액(totalPrice)은 필수 항목입니다.")
	@Positive(message = "총 주문 금액(totalPrice)은 0보다 커야 합니다.")
	private Long totalPrice;

	@NotEmpty(message = "상품(products)은 최소 하나 이상 포함되어야 합니다.")
	private List<@Valid OrderProductUpdateRequestDto> products;

	@Getter
	@Setter
	@NoArgsConstructor
	public static class OrderProductUpdateRequestDto implements ProductRequestDto {

		@NotBlank(message = "productId는 필수 항목입니다.")
		private String productId;

		@NotBlank(message = "상품명(name)은 필수 항목입니다.")
		private String name;
		
		@NotNull(message = "상품 개수(quantity)는 필수 항목입니다.")
		@Min(value = 1, message = "상품 개수(quantity)는 1 이상이어야 합니다.")
		private Integer quantity;

		@NotNull(message = "상품 가격(price)은 필수 항목입니다.")
		@Positive(message = "상품 가격(price)은 0보다 커야 합니다.")
		private Long price;
	}
}