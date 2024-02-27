package com.example.demo;

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class DemoApplication {

	@Bean
	@Profile("!MistralAI")
	ChatLanguageModel openAIChatLanguageModel() {
		return AzureOpenAiChatModel.builder()
				.endpoint(System.getenv("AZURE_OPENAI_ENDPOINT"))
				.apiKey(System.getenv("AZURE_OPENAI_KEY"))
				.deploymentName("gpt-4")
				.logRequestsAndResponses(true)
				.build();
	}

	@Bean
	@Profile("MistralAI")
	ChatLanguageModel mistralAIChatLanguageModel() {
		return MistralAiChatModel.builder()
				.baseUrl(System.getenv("MISTRAL_AI_BASE_URL"))
				.apiKey(System.getenv("MISTRAL_AI_KEY"))
				.temperature(0.1)
				.logRequests(true)
				.logResponses(true)
				.build();
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
