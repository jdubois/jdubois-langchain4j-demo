package com.example.demo.configuration.azure;

import com.azure.identity.DefaultAzureCredentialBuilder;
import dev.langchain4j.model.azure.AzureOpenAiEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("azure")
public class AzureEmbeddingModelConfiguration {

    @Value("${AZURE_OPENAI_ENDPOINT}")
    private String azureOpenAiEndpoint;

    @Bean
    EmbeddingModel azureOpenAiEmbeddingModel() {
        return AzureOpenAiEmbeddingModel.builder()
                .endpoint(azureOpenAiEndpoint)
                .tokenCredential(new DefaultAzureCredentialBuilder().build())
                .deploymentName("text-embedding-ada")
                .logRequestsAndResponses(true)
                .build();
    }
}
