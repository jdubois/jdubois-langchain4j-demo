package com.example.demo.configuration.azure;

import com.azure.identity.DefaultAzureCredentialBuilder;
import dev.langchain4j.model.azure.AzureOpenAiChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("azure")
public class AzureChatModelConfiguration {

    @Value("${AZURE_OPENAI_ENDPOINT}")
    private String azureOpenAiEndpoint;

    @Bean
    ChatLanguageModel azureOpenAIChatLanguageModel() {
        return AzureOpenAiChatModel.builder()
                .endpoint(azureOpenAiEndpoint)
                .tokenCredential(new DefaultAzureCredentialBuilder().build())
                .deploymentName("gpt-4o")
                .logRequestsAndResponses(true)
                .build();
    }
}
