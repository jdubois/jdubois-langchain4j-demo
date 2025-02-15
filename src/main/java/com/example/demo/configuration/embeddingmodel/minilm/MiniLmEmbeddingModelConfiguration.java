package com.example.demo.configuration.embeddingmodel.minilm;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("small")
public class MiniLmEmbeddingModelConfiguration {

    @Bean
    EmbeddingModel miniLmEmbeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }
}
