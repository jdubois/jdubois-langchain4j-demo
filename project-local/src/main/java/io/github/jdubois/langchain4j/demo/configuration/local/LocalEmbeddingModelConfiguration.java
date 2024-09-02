package io.github.jdubois.langchain4j.demo.configuration.local;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class LocalEmbeddingModelConfiguration {

    @Bean
    @Profile("local")
    EmbeddingModel ollamaEmbeddingModel() {
        return OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434/")
                .modelName("nomic-embed-text")
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
