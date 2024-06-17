package com.example.demo.configuration.azure;

import com.azure.identity.DefaultAzureCredentialBuilder;
import dev.langchain4j.model.azure.AzureOpenAiImageModel;
import dev.langchain4j.model.image.ImageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("azure")
public class AzureImageModelConfiguration {

    @Value("${AZURE_OPENAI_ENDPOINT}")
    private String azureOpenAiEndpoint;

    @Bean
    ImageModel imageModel() {
        return AzureOpenAiImageModel.builder()
                .endpoint(azureOpenAiEndpoint)
                .tokenCredential(new DefaultAzureCredentialBuilder().build())
                .deploymentName("dall-e-3")
                .logRequestsAndResponses(true)
                .build();
    }
}
