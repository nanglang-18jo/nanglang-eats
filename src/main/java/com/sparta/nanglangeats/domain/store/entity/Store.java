package com.sparta.nanglangeats.domain.store.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sparta.nanglangeats.domain.address.entity.CommonAddress;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	private UUID uuid; // 노출되는 식별자

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

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

	@Column(nullable = false)
	private Boolean isActive;

	@OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
	private List<StoreImage> storeImages = new ArrayList<>();

	@Builder
	public Store(Category category, User user, String name, LocalTime openTime, LocalTime closeTime,
		CommonAddress commonAddress, String addressDetail, String phoneNumber) {
		this.uuid = UUID.randomUUID(); // uuid 자동 생성
		this.category = category;
		this.user = user;
		this.name = name;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.commonAddress = commonAddress;
		this.addressDetail = addressDetail;
		this.phoneNumber = phoneNumber;
		this.rating = null; // 초기에는 별점을 null로 두고, 프론트에서도 안 보여주도록 함
		this.reviewCount = 0;
		this.isActive = true;
	}

	/**
	 * 연관관계 편의 메서드
	 */
	public void addStoreImage(StoreImage storeImage) {
		this.storeImages.add(storeImage);
		storeImage.setStoreTo(this);
	}
}
