package com.example.demo.configuration.local;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class LocalEmbeddingStoreConfiguration {

    @Bean
    EmbeddingStore<TextSegment> embeddingStore() {
        return QdrantEmbeddingStore.builder()
                .collectionName("kbindex")
                .host("localhost")
                .port(6334)
                .build();
    }
}
