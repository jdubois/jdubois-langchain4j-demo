package com.example.demo.configuration;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.util.logging.Logger;

/**
 * Base configuration class for setting up API endpoints and keys
 * for either Microsoft Foundry or a local Ollama instance.
 */
public abstract class AbstractModelConfiguration {

    final Logger log = Logger.getLogger(AbstractModelConfiguration.class.getName());

    @Value("${OPENAI_BASE_URL:}")
    String microsoftFoundryEndpoint;

    @Value("${OPENAI_API_KEY:}")
    String microsoftFoundryApiKey;

    @Value("${ollama.port:11434}")
    String ollamaPort;

    String endpoint;

    String apiKey;

    String modelName;

    @PostConstruct
    void logConfiguration() {
        if (microsoftFoundryEndpoint == null || microsoftFoundryEndpoint.isBlank()) {
            log.info("Using local Ollama instance at port " + ollamaPort);
            this.endpoint = "http://localhost:" + ollamaPort + "/v1";
            this.apiKey = "";
            this.modelName = getOllamaModelName();
        } else {
            log.info("Using Microsoft Foundry endpoint: " + microsoftFoundryEndpoint);
            this.endpoint = microsoftFoundryEndpoint;
            this.apiKey = microsoftFoundryApiKey;
            this.modelName = getMicrosoftFoundryModelName();
        }
    }

    abstract String getMicrosoftFoundryModelName();

    abstract String getOllamaModelName();
}
