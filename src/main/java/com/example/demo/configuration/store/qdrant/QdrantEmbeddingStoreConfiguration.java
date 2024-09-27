package com.example.demo.configuration.store.qdrant;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
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
@Profile({"small", "good", "github"})
public class QdrantEmbeddingStoreConfiguration {

    private static final String COLLECTION_NAME = "kbindex";

    private EmbeddingModel embeddingModel;

    public QdrantEmbeddingStoreConfiguration(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Bean
    EmbeddingStore<TextSegment> embeddingStore() throws ExecutionException, InterruptedException {

        // the Collection should have the same dimension as the embedding model
        int dimensions = 768;
        if (embeddingModel instanceof DimensionAwareEmbeddingModel) {
            dimensions = ((DimensionAwareEmbeddingModel) embeddingModel).dimension();
        }

        QdrantClient client =
                new QdrantClient(QdrantGrpcClient.newBuilder("localhost", 6334, false).build());

        if (!client.listCollectionsAsync().get().contains(COLLECTION_NAME)) {
            client.createCollectionAsync(COLLECTION_NAME,
                            Collections.VectorParams.newBuilder()
                                    .setDistance(Collections.Distance.Cosine)
                                    .setSize(dimensions)
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
