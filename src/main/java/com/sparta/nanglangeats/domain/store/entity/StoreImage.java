package com.sparta.nanglangeats.domain.store.entity;

import com.sparta.nanglangeats.domain.image.service.dto.ImageResponse;
import com.sparta.nanglangeats.global.common.entity.Timestamped;

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
@Table(name = "p_store_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreImage extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@Column(nullable = false)
	private String fileName;

	@Column(nullable = false)
	private String url;

	@Builder
	public StoreImage(Store store, ImageResponse response) {
		this.store = store;
		this.fileName = response.getFileName();
		this.url = response.getUrl();
	}

	/**
	 * 연관관계 편의 메서드
	 */
	public void setStoreTo(Store store) {
		this.store = store;
	}
}
