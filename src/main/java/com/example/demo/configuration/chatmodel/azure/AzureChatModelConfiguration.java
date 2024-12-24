package com.example.demo.configuration.chatmodel.azure;

import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Set;

import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;

@Configuration
@Profile("azure")
public class AzureChatModelConfiguration {

    @Value("${AZURE_OPENAI_ENDPOINT}")
    private String azureOpenAiEndpoint;

    @Value("${AZURE_OPENAI_KEY}")
    private String azureOpenAiKey;

    @Bean
    ChatLanguageModel azureOpenAIChatLanguageModel() {
        return AzureOpenAiChatModel.builder()
                .endpoint(azureOpenAiEndpoint)
                .apiKey(azureOpenAiKey)
                .deploymentName("gpt-4o")
                .supportedCapabilities(Set.of(RESPONSE_FORMAT_JSON_SCHEMA))
                .logRequestsAndResponses(true)
                .strictJsonSchema(true)
                .build();
    }
}
