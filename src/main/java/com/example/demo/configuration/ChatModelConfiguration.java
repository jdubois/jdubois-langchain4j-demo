package com.example.demo.configuration;

import com.azure.identity.DefaultAzureCredentialBuilder;
import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ChatModelConfiguration {

    @Value("${AZURE_OPENAI_ENDPOINT}")
    private String azureOpenAiEndpoint;

    @Bean
    @Profile("azure")
    ChatLanguageModel azureOpenAIChatLanguageModel() {
        return AzureOpenAiChatModel.builder()
                .endpoint(azureOpenAiEndpoint)
                .tokenCredential(new DefaultAzureCredentialBuilder().build())
                .deploymentName("gpt-4o")
                .logRequestsAndResponses(true)
                .build();
    }

    @Bean
    @Profile("local")
    ChatLanguageModel mistralAIChatLanguageModel() {
        return MistralAiChatModel.builder()
                .baseUrl(System.getenv("MISTRAL_AI_BASE_URL"))
                .apiKey(System.getenv("MISTRAL_AI_KEY"))
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
