package com.example.demo.configuration.embeddingmodel.github;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialEmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("github")
public class GitHubModelsEmbeddingModelConfiguration {

    @Bean
    EmbeddingModel ollamaEmbeddingModel() {
        return OpenAiOfficialEmbeddingModel.builder()
                .isGitHubModels(true)
                .modelName(com.openai.models.embeddings.EmbeddingModel.TEXT_EMBEDDING_3_SMALL)
                .build();
    }
}
