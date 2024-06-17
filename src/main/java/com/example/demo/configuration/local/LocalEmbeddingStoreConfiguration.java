package com.example.demo.configuration.local;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.ExecutionException;

@Configuration
@Profile("local")
public class LocalEmbeddingStoreConfiguration {

    private static final String COLLECTION_NAME = "kbindex";

    @Bean
    EmbeddingStore<TextSegment> embeddingStore() throws ExecutionException, InterruptedException {
        QdrantClient client =
                new QdrantClient(QdrantGrpcClient.newBuilder("localhost", 6334, false).build());

        if (!client.listCollectionsAsync().get().contains(COLLECTION_NAME)) {
            client.createCollectionAsync(COLLECTION_NAME,
                            Collections.VectorParams.newBuilder()
                                    .setDistance(Collections.Distance.Cosine)
                                    .setSize(384)
                                    .build())
                    .get();
        }

        return QdrantEmbeddingStore.builder()
                .collectionName(COLLECTION_NAME)
                .host("localhost")
                .port(6334)
                .build();
    }
}
