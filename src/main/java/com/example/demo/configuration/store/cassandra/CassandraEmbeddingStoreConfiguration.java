package com.example.demo.configuration.store.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ExecutionInfo;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.cassandra.CassandraEmbeddingStore;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.InetSocketAddress;
import java.util.Collections;

import static com.dtsx.astra.sdk.cassio.CassandraSimilarityMetric.COSINE;

@Configuration
@Profile("cassandra")
public class CassandraEmbeddingStoreConfiguration {

    private EmbeddingModel embeddingModel;


    @Bean
    EmbeddingStore<TextSegment> embeddingStore() {

        // the Collection should have the same dimension as the embedding model
        int dimensions = 768;
        if (embeddingModel instanceof DimensionAwareEmbeddingModel) {
            dimensions = ((DimensionAwareEmbeddingModel) embeddingModel).dimension();
        }

        // Part of Database Creation, creating keyspace
        final InetSocketAddress contactPoint = new InetSocketAddress("localhost", 9042);
        ExecutionInfo info = CqlSession.builder()
                .addContactPoint(contactPoint)
                .withLocalDatacenter("DC1")
                .build().execute(
                        "CREATE KEYSPACE IF NOT EXISTS langchain4j" +
                                " WITH replication = {'class':'SimpleStrategy', 'replication_factor':'1'};").getExecutionInfo();


        return CassandraEmbeddingStore.builder()
                .contactPoints(Collections.singletonList(contactPoint.getHostName()))
                .port(contactPoint.getPort())
                .localDataCenter("DC1")
                .keyspace("langchain4j")
                .table("kbindex")
                .dimension(dimensions)
                .build();
    }
}
