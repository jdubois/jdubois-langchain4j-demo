package com.example.demo.configuration;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.azure.search.AzureAiSearchEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingStoreConfiguration {

    @Bean
    EmbeddingStore<TextSegment> embeddingStore() {
        return AzureAiSearchEmbeddingStore.builder()
                .endpoint(System.getenv("AZURE_SEARCH_ENDPOINT"))
                .apiKey(System.getenv("AZURE_SEARCH_KEY"))
                .dimensions(1536)
                .build();
    }
}
