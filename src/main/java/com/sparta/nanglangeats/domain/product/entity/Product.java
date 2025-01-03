package com.sparta.nanglangeats.domain.product.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.nanglangeats.domain.image.service.dto.ImageResponse;
import com.sparta.nanglangeats.domain.product.controller.dto.request.ProductRequest;
import com.sparta.nanglangeats.domain.store.entity.Store;
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
@Table(name = "p_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String uuid; // 노출되는 식별자

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@Column(nullable = false)
	private String name; // 상품명

	@Column
	private String description;

	@Column(nullable = false)
	private int price;

	@Column
	private String thumbnailUrl;

	@Column
	private String thumbnailName;

	@Column(nullable = false)
	private Boolean isPublic;

	@Column(nullable = false)
	private Boolean isActive;

	@Builder
	public Product(Store store, String name, String description, int price, String thumbnailUrl, String thumbnailName) {
		this.uuid = UUID.randomUUID().toString();
		this.store = store;
		this.name = name;
		this.description = description;
		this.price = price;
		this.thumbnailUrl = thumbnailUrl;
		this.thumbnailName = thumbnailName;
		this.isPublic = true;
		this.isActive = true;
	}

	public void update(ProductRequest request, ImageResponse imageResponse) {
		this.name = request.getName();
		this.description = request.getDescription();
		this.price = request.getPrice();
		if(imageResponse != null) {
			this.thumbnailName = imageResponse.getFileName();
			this.thumbnailUrl = imageResponse.getUrl();
		}
	}

	public void delete(String deletedBy) {
		this.isPublic = false;
		this.isActive = false;
		this.setDeletedAt(LocalDateTime.now());
		this.setDeletedBy(deletedBy);
	}

	public void toggleVisibility() {
		this.isPublic = !this.isPublic;
	}
}
