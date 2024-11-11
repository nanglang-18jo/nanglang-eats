package com.sparta.nanglangeats.domain.image.service.dto;

import lombok.Getter;

@Getter
public class ImageResponse {
	private final String fileName;
	private final String url;

	public ImageResponse(String fileName, String url) {
		this.fileName = fileName;
		this.url = url;
	}
}
