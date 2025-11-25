package com.example.demo.configuration.embeddingmodel.ollama;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"local"})
public class OllamaEmbeddingModelConfiguration {

    @Bean
    EmbeddingModel ollamaEmbeddingModel() {
        return OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434/")
                .modelName("nomic-embed-text")
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
