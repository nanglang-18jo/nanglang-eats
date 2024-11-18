package com.sparta.nanglangeats.domain.ai.service;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GoogleApi {

	private RestTemplate restTemplate;

	@Value("${google.ai.api-key}")
	private String apiKey;

	public GoogleApi(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	public String generateText(String prompt) {
		URI uri = UriComponentsBuilder.fromUriString("https://generativelanguage.googleapis.com")
			.path("/v1beta/models/gemini-1.5-flash:generateContent")
			.queryParam("key", apiKey).encode().build().toUri();

		String test1 = String.format("{\"contents\" : [{\"parts\" : [{\"text\" : \"%s\"}]}]}",prompt);

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(uri, new HttpEntity<>(test1),
			String.class);

		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = null;
		try {
			rootNode = objectMapper.readTree(responseEntity.getBody());
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		// "parts[0].text" 경로 값 추출
		String textValue = rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
		return textValue;
	}
}
