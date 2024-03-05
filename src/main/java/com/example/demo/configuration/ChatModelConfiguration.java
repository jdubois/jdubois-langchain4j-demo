package com.example.demo.configuration;

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatModelConfiguration {

    @Bean
    ChatLanguageModel openAIChatLanguageModel() {
        return AzureOpenAiChatModel.builder()
                .endpoint(System.getenv("AZURE_OPENAI_ENDPOINT"))
                .apiKey(System.getenv("AZURE_OPENAI_KEY"))
                .deploymentName("gpt-4")
                .logRequestsAndResponses(true)
                .build();
    }

    ChatLanguageModel mistralAIChatLanguageModel() {
        return MistralAiChatModel.builder()
                .baseUrl(System.getenv("MISTRAL_AI_BASE_URL"))
                .apiKey(System.getenv("MISTRAL_AI_KEY"))
                .temperature(0.1)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
