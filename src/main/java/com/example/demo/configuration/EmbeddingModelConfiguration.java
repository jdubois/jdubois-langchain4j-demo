package com.example.demo.configuration;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialEmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import static com.openai.models.embeddings.EmbeddingModel.TEXT_EMBEDDING_3_SMALL;

@Configuration
public class EmbeddingModelConfiguration extends AbstractModelConfiguration {

    public static final String MICROSOFT_FOUNDRY_MODEL_NAME = TEXT_EMBEDDING_3_SMALL.asString();
    public static final String OLLAMA_MODEL_NAME = "nomic-embed-text";

    @Bean
    EmbeddingModel openAiEmbeddingModel() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        EmbeddingModel embeddingModel = OpenAiOfficialEmbeddingModel.builder()
                .baseUrl(endpoint)
                .apiKey(apiKey)
                .modelName(modelName)
                .build();

        stopWatch.stop();
        log.info(String.format("OpenAI Embedding Model created in %.3f seconds", stopWatch.getTotalTimeSeconds()));
        return embeddingModel;
    }

    @Override
    String getMicrosoftFoundryModelName() {
        return MICROSOFT_FOUNDRY_MODEL_NAME;
    }

    @Override
    String getOllamaModelName() {
        return OLLAMA_MODEL_NAME;
    }
}
