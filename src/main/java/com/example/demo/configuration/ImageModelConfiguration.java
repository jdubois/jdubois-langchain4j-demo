package com.example.demo.configuration;

import dev.langchain4j.model.azure.AzureOpenAiImageModel;
import dev.langchain4j.model.image.ImageModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageModelConfiguration {

    @Bean
    ImageModel imageModel() {
        return AzureOpenAiImageModel.builder()
                .endpoint(System.getenv("AZURE_OPENAI_ENDPOINT"))
                .apiKey(System.getenv("AZURE_OPENAI_KEY"))
                .deploymentName("dall-e-3")
                .logRequestsAndResponses(true)
                .build();
    }
}
