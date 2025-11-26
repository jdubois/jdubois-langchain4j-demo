package com.example.demo.configuration.store.elasticsearch;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class ElasticsearchEmbeddingStoreConfiguration {

    @Value("${elasticsearch.port:9200}")
    private int elasticsearchPort;

    @Bean
    RestClient restClient() {
        return RestClient.builder(
                new HttpHost("localhost", elasticsearchPort, "http")
        ).build();
    }

    @Bean
    EmbeddingStore<TextSegment> embeddingStore(RestClient restClient) {
        return ElasticsearchEmbeddingStore.builder()
                .restClient(restClient)
                .build();
    }
}
