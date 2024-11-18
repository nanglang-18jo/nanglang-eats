package com.sparta.nanglangeats.domain.delivery_address.entity;

import com.sparta.nanglangeats.domain.address.entity.CommonAddress;
import com.sparta.nanglangeats.domain.delivery_address.controller.dto.request.DeliveryAddressUpdateRequest;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.entity.Timestamped;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name ="p_delivery_address")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryAddress extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private CommonAddress commonAddress;

	@Column
	private String addressDetail;

	@Column
	private String alias;

	@Column
	private boolean isRecentDelivery;

	@Builder
	private DeliveryAddress(User user, CommonAddress commonAddress, String addressDetail, String alias, boolean isRecentDelivery) {
		this.user = user;
		this.commonAddress = commonAddress;
		this.addressDetail = addressDetail;
		this.alias = alias;
		this.isRecentDelivery = isRecentDelivery;
	}

	public void update(CommonAddress commonAddress, DeliveryAddressUpdateRequest request) {
		this.commonAddress = commonAddress;
		this.addressDetail = request.getAddressDetail();
		this.alias = request.getAlias();
		this.isRecentDelivery = request.getIsRecentDelivery();
	}

	public void updateIsRecentDelivery(boolean isRecentDelivery) {
		this.isRecentDelivery = isRecentDelivery;
	}
}