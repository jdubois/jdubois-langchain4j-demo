package com.example.demo.configuration.embeddingmodel.azure;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialEmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.openai.models.embeddings.EmbeddingModel.TEXT_EMBEDDING_3_SMALL;

@Configuration
@Profile("azure")
public class AzureEmbeddingModelConfiguration {

    @Value("${AZURE_OPENAI_ENDPOINT}")
    private String azureOpenAiEndpoint;

    @Value("${AZURE_OPENAI_KEY}")
    private String azureOpenAiKey;

    @Bean
    EmbeddingModel azureOpenAiEmbeddingModel() {
        return OpenAiOfficialEmbeddingModel.builder()
                .baseUrl(azureOpenAiEndpoint)
                .apiKey(azureOpenAiKey)
                .modelName(TEXT_EMBEDDING_3_SMALL)
                .build();
    }
}
