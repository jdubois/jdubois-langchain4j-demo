package com.example.demo.configuration.store;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.azure.search.AzureAiSearchEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingStoreConfiguration {

    @Value("${AZURE_SEARCH_ENDPOINT}")
    private String azureSearchEndpoint;

    @Value("${AZURE_SEARCH_KEY}")
    private String azureSearchKey;

    @Bean
    EmbeddingStore<TextSegment> embeddingStore() {
        return AzureAiSearchEmbeddingStore.builder()
                .endpoint(azureSearchEndpoint)
                .apiKey(azureSearchKey)
                .dimensions(1536)
                .build();
    }
}