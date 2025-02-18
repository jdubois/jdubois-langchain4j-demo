package com.example.demo.configuration.imagemodel.azure;

import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialImageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("azure")
public class AzureImageModelConfiguration {

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
