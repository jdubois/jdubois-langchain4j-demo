package com.example.demo;

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	@Bean
	ChatLanguageModel chatLanguageModel() {
		return AzureOpenAiChatModel.builder()
				.endpoint(System.getenv("AZURE_OPENAI_ENDPOINT"))
				.apiKey(System.getenv("AZURE_OPENAI_KEY"))
				.deploymentName("gpt-4")
				.logRequestsAndResponses(true)
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
