package com.example.demo.configuration.model.github;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.github.GitHubModelsEmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static dev.langchain4j.model.github.GitHubModelsEmbeddingModelName.TEXT_EMBEDDING_3_SMALL;

@Configuration
@Profile("github")
public class GitHubModelsEmbeddingModelConfiguration {

    @Value("${GITHUB_TOKEN}")
    private String gitHubToken;

    @Bean
    EmbeddingModel ollamaEmbeddingModel() {
        return GitHubModelsEmbeddingModel.builder()
                .gitHubToken(gitHubToken)
                .modelName(TEXT_EMBEDDING_3_SMALL)
                .logRequestsAndResponses(true)
                .build();
    }
}
