package com.sparta.nanglangeats.domain.store.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import com.sparta.nanglangeats.domain.address.entity.CommonAddress;
import com.sparta.nanglangeats.domain.image.service.dto.ImageResponse;
import com.sparta.nanglangeats.domain.store.controller.dto.request.StoreRequest;
import com.sparta.nanglangeats.domain.user.entity.User;
import com.sparta.nanglangeats.global.common.entity.Timestamped;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_store")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String uuid; // 노출되는 식별자

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "owner_id")
	private User owner;

	@Column(nullable = false)
	private String name; // 상호명

	@Column(nullable = false)
	private LocalTime openTime; // 가게 운영 시작 시간

	@Column(nullable = false)
	private LocalTime closeTime; // 가게 운영 종료 시간

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "common_address_id")
	private CommonAddress commonAddress; // 기본 주소

	@Column
	private String addressDetail; // 상세 주소

	@Column
	private String phoneNumber; // 전화번호

	@Column
	private Float rating; // 별점

	@Column(nullable = false)
	private Integer reviewCount;

	@Column
	private String thumbnailName;

	@Column
	private String thumbnailUrl;

	@Column(nullable = false)
	private Boolean isActive;

	@Builder
	public Store(Category category, User owner, String name, LocalTime openTime, LocalTime closeTime,
		CommonAddress commonAddress, String addressDetail, String phoneNumber, String thumbnailUrl,
		String thumbnailName) {
		this.uuid = UUID.randomUUID().toString(); // uuid 자동 생성
		this.category = category;
		this.owner = owner;
		this.name = name;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.commonAddress = commonAddress;
		this.addressDetail = addressDetail;
		this.phoneNumber = phoneNumber;
		this.rating = null; // 초기에는 별점을 null로 두고, 프론트에서도 안 보여주도록 함
		this.reviewCount = 0;
		this.isActive = true;
		this.thumbnailUrl = thumbnailUrl;
		this.thumbnailName = thumbnailName;
	}

	public void update(StoreRequest request, Category category, CommonAddress commonAddress,
		ImageResponse imageResponse) {
		this.category = category;
		this.commonAddress = commonAddress;
		this.name = request.getName();
		this.openTime = request.getOpenTime();
		this.closeTime = request.getCloseTime();
		this.addressDetail = request.getAddressDetail();
		this.phoneNumber = request.getPhoneNumber();
		if (imageResponse != null) {
			this.thumbnailName = imageResponse.getFileName();
			this.thumbnailUrl = imageResponse.getUrl();
		}
	}

	public void delete(String deletedBy) {
		this.isActive = false;
		this.setDeletedAt(LocalDateTime.now());
		this.setDeletedBy(deletedBy);
	}

	public void updateRating(int rating) {
		if (this.reviewCount == 0)
			this.rating = (float)rating;
		else {
			this.rating = (this.rating * this.reviewCount + rating) / (this.reviewCount + 1);
		}
		increaseReviewCount();
	}

	private void increaseReviewCount() {
		this.reviewCount++;
	}

}
