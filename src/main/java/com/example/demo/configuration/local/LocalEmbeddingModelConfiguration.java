package com.example.demo.configuration.local;

import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class LocalEmbeddingModelConfiguration {

    @Bean
    EmbeddingModel localEmbeddingModel() {
        return new AllMiniLmL6V2QuantizedEmbeddingModel();
    }
}
