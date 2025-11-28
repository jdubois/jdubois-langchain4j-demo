package com.example.demo.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

import static com.openai.models.ChatModel.GPT_5_MINI;
import static com.openai.models.embeddings.EmbeddingModel.TEXT_EMBEDDING_3_SMALL;
import static com.openai.models.images.ImageModel.DALL_E_3;

@Service
public class ModelsDiscoveryService {

    public static final String LOCAL_OPENAI_BASE_URL = "http://localhost:11434/v1";;
    public static final String LOCAL_CHAT_MODEL = "mistral:7b";
    public static final String LOCAL_EMBEDDING_MODEL = "nomic-embed-text";

    private final Logger log = Logger.getLogger(ModelsDiscoveryService.class.getName());

    @Value("${OPENAI_BASE_URL:}")
    String openAiBaseUrl;

    @Value("${OPENAI_API_KEY:}")
    String openAiApiKey;

    @Value("${CHAT_MODEL_NAME:}")
    String chatModelName;

    @Value("${EMBEDDING_MODEL_NAME:}")
    String embeddingModelName;

    @Value("${IMAGE_MODEL_NAME:}")
    String imageModelName;

    public String getOpenAiBaseUrl() {
        String resolvedOpenAiBaseUrl;
        if (openAiBaseUrl != null && !openAiBaseUrl.isBlank()) {
            resolvedOpenAiBaseUrl = openAiBaseUrl;
        } else {
            resolvedOpenAiBaseUrl = LOCAL_OPENAI_BASE_URL;
        }
        return resolvedOpenAiBaseUrl;
    }

    public String getOpenAiApiKey() {
        return openAiApiKey;
    }

    public String getChatModelName() {
        String resolvedChatModelName;
        if (chatModelName != null && !chatModelName.isBlank()) {
            resolvedChatModelName = chatModelName;
        } else if (getOpenAiBaseUrl().contains("localhost")) {
            resolvedChatModelName = LOCAL_CHAT_MODEL;
        } else {
            resolvedChatModelName = GPT_5_MINI.asString();
        }
        return resolvedChatModelName;
    }

    public String getEmbeddingModelName() {
        String resolvedEmbeddingModelName;
        if (embeddingModelName != null && !embeddingModelName.isBlank()) {
            resolvedEmbeddingModelName = embeddingModelName;
        } else if (getOpenAiBaseUrl().contains("localhost")) {
            resolvedEmbeddingModelName = LOCAL_EMBEDDING_MODEL;
        } else {
            resolvedEmbeddingModelName = TEXT_EMBEDDING_3_SMALL.asString();
        }
        return resolvedEmbeddingModelName;
    }

    public String getImageModelName() {
        String resolvedImageModelName;
        if (imageModelName != null && !imageModelName.isBlank()) {
            resolvedImageModelName = imageModelName;
        } else if (getOpenAiBaseUrl().contains("localhost")) {
            resolvedImageModelName = "";
        } else {
            resolvedImageModelName = DALL_E_3.asString();
        }
        return resolvedImageModelName;
    }

    @PostConstruct
    public void printModelConfiguration() {
        log.info("------------------------------- configuration -------------------------------");
        log.info("OpenAI Base URL:       " + getOpenAiBaseUrl());
        log.info("Chat Model Name:       " + getChatModelName());
        log.info("Embedding Model Name:  " + getEmbeddingModelName());
        log.info("Image Model Name:      " + getImageModelName());
        log.info("-------------------------------------------------------------------------------");
    }
}
