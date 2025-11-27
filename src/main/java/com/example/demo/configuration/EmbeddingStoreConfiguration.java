package com.example.demo.configuration;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.azure.search.AzureAiSearchEmbeddingStore;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import java.util.logging.Logger;

@Configuration
public class EmbeddingStoreConfiguration {

    private final Logger log = Logger.getLogger(EmbeddingStoreConfiguration.class.getName());

    @Value("${AZURE_SEARCH_ENDPOINT:}")
    private String azureSearchEndpoint;

    @Value("${AZURE_SEARCH_KEY:}")
    private String azureSearchKey;

    @Value("${elasticsearch.port:9200}")
    private int elasticsearchPort;

    @Bean
    EmbeddingStore<TextSegment> embeddingStore(RestClient restClient) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        EmbeddingStore<TextSegment> embeddingStore;
        if (!azureSearchEndpoint.isBlank() && !azureSearchKey.isBlank()) {
            log.info("Using Azure AI Search Embedding Store");
            embeddingStore = AzureAiSearchEmbeddingStore.builder()
                    .endpoint(azureSearchEndpoint)
                    .apiKey(azureSearchKey)
                    .dimensions(1536)
                    .build();
        } else {
            log.info("Using Elasticsearch Embedding Store");
            embeddingStore = ElasticsearchEmbeddingStore.builder()
                    .restClient(restClient)
                    .build();
        }
        stopWatch.stop();
        log.info(String.format("OpenAI Embedding Store created in %.3f seconds", stopWatch.getTotalTimeSeconds()));
        return embeddingStore;
    }

    @Bean
    RestClient restClient() {
        return RestClient.builder(
                new HttpHost("localhost", elasticsearchPort, "http")
        ).build();
    }
}
