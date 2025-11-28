package com.example.demo.configuration;

import com.example.demo.service.ModelsDiscoveryService;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialEmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import java.util.logging.Logger;

@Configuration
public class EmbeddingModelConfiguration {

    private final Logger log = Logger.getLogger(EmbeddingModelConfiguration.class.getName());

    @Bean
    EmbeddingModel openAiEmbeddingModel(ModelsDiscoveryService modelsDiscoveryService) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        EmbeddingModel embeddingModel = OpenAiOfficialEmbeddingModel.builder()
                .baseUrl(modelsDiscoveryService.getOpenAiBaseUrl())
                .apiKey(modelsDiscoveryService.getOpenAiApiKey())
                .modelName(modelsDiscoveryService.getEmbeddingModelName())
                .build();

        stopWatch.stop();
        log.info(String.format("OpenAI Embedding Model created in %.3f seconds", stopWatch.getTotalTimeSeconds()));
        return embeddingModel;
    }
}
