package com.example.demo.configuration;

import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialImageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageModelConfiguration {

    @Value("${AZURE_OPENAI_ENDPOINT}")
    private String azureOpenAiEndpoint;

    @Value("${AZURE_OPENAI_KEY}")
    private String azureOpenAiKey;

    @Bean
    ImageModel imageModel() {
        return OpenAiOfficialImageModel.builder()
                .baseUrl(azureOpenAiEndpoint)
                .azureApiKey(azureOpenAiKey)
                .modelName("dall-e-3")
                .build();
    }
}
