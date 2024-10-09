package com.example.demo.configuration.store.elasticsearch;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("elasticsearch")
public class ElasticsearchEmbeddingStoreConfiguration {

    @Bean
    EmbeddingStore<TextSegment> embeddingStore() {
        RestClient client = RestClient.builder(new HttpHost("localhost", 9200))
                .build();

        return ElasticsearchEmbeddingStore.builder()
                .indexName("kbindex")
                .restClient(client)
                .build();
    }
}
