package com.sparta.nanglangeats.domain.image.entity;

import com.sparta.nanglangeats.domain.image.enums.ImageCategory;
import com.sparta.nanglangeats.domain.image.service.dto.ImageResponse;
import com.sparta.nanglangeats.global.common.entity.Timestamped;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String fileName;

	@Column(nullable = false)
	private String url;

	@Column(nullable = false)
	private Long contentId;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ImageCategory imageCategory;

	@Builder
	public Image(ImageResponse response, Long contentId, ImageCategory imageCategory) {
		this.fileName = response.getFileName();
		this.url = response.getUrl();
		this.contentId = contentId;
		this.imageCategory = imageCategory;
	}
}
