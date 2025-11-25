package com.example.demo.configuration.imagemodel.azure;

import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialImageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.openai.models.images.ImageModel.DALL_E_3;

@Configuration
@Profile("azure")
public class AzureImageModelConfiguration {

    @Value("${OPENAI_BASE_URL}")
    private String microsoftFoundryEndpoint;

    @Value("${OPENAI_API_KEY}")
    private String microsoftFoundryApiKey;

    @Bean
    ImageModel imageModel() {
        return OpenAiOfficialImageModel.builder()
                .baseUrl(microsoftFoundryEndpoint)
                .apiKey(microsoftFoundryApiKey)
                .modelName(DALL_E_3)
                .build();
    }
}
