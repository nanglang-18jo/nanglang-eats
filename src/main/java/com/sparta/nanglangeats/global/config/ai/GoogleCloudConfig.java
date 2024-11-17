package com.sparta.nanglangeats.global.config.ai;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleCloudConfig {

	@Value("${GOOGLE_AI_PROJECT_ID}")
	private String projectId;

	@Bean
	public TextGenerationServiceClient textGenerationServiceClient() throws IOException {
		return TextGenerationServiceClient.create();
	}
}