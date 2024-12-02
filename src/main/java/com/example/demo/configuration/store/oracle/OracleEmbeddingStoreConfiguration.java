package com.example.demo.configuration.store.oracle;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.oracle.CreateOption;
import dev.langchain4j.store.embedding.oracle.OracleEmbeddingStore;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.SQLException;

@Configuration
@Profile("oracle")
public class OracleEmbeddingStoreConfiguration {

    @Value("${ORACLE_JDBC_URL}")
    private String url;

    @Value("${ORACLE_JDBC_USER}")
    private String user;

    @Value("${ORACLE_JDBC_PASSWORD}")
    private String password;

    @Bean
    EmbeddingStore<TextSegment> embeddingStore() throws SQLException {

        PoolDataSource dataSource = PoolDataSourceFactory.getPoolDataSource();
        dataSource.setConnectionFactoryClassName(
                "oracle.jdbc.datasource.impl.OracleDataSource");
        dataSource.setURL(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        return OracleEmbeddingStore.builder()
                .dataSource(dataSource)
                .embeddingTable("profile_oracle",
                        CreateOption.CREATE_OR_REPLACE)
                .build();

    }
}