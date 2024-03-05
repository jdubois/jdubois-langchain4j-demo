package com.example.demo.configuration;

import dev.langchain4j.model.azure.AzureOpenAiEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static dev.langchain4j.model.azure.AzureOpenAiModelName.TEXT_EMBEDDING_ADA_002;

@Configuration
public class EmbeddingModelConfiguration {

    @Bean
    EmbeddingModel embeddingModel() {
        return AzureOpenAiEmbeddingModel.builder()
                .endpoint(System.getenv("AZURE_OPENAI_ENDPOINT"))
                .apiKey(System.getenv("AZURE_OPENAI_KEY"))
                .deploymentName("text-embedding-ada-002")
                .tokenizer(new OpenAiTokenizer(TEXT_EMBEDDING_ADA_002))
                .logRequestsAndResponses(true)
                .build();
    }
}
