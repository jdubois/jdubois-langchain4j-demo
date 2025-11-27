package com.example.demo.controller;

import com.example.demo.configuration.ChatModelConfiguration;
import com.example.demo.configuration.EmbeddingModelConfiguration;
import com.example.demo.configuration.ImageModelConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the home page.
 */
@Controller
public class HomeController {


    @Value("${OPENAI_BASE_URL:}")
    String microsoftFoundryEndpoint;

    @Value("${OPENAI_API_KEY:}")
    String microsoftFoundryApiKey;

    @Value("${ollama.port:11434}")
    String ollamaPort;

    @Value("${AZURE_SEARCH_ENDPOINT:}")
    private String azureSearchEndpoint;

    @GetMapping("/")
    public String home(Model model) {
        String openaiBaseUrl;
        String openaiApiKeyMasked;
        String chatModelName;
        String imageModelName;
        String embeddingModelName;
        String vectorStore;
        if (microsoftFoundryEndpoint == null || microsoftFoundryEndpoint.isBlank()) {
            openaiBaseUrl = "http://localhost:" + ollamaPort + "/v1";
            chatModelName = ChatModelConfiguration.OLLAMA_MODEL_NAME;
            imageModelName = ImageModelConfiguration.OLLAMA_MODEL_NAME;
            embeddingModelName = EmbeddingModelConfiguration.OLLAMA_MODEL_NAME;
        } else {
            openaiBaseUrl = microsoftFoundryEndpoint;
            chatModelName = ChatModelConfiguration.MICROSOFT_FOUNDRY_MODEL_NAME;
            imageModelName = ImageModelConfiguration.MICROSOFT_FOUNDRY_MODEL_NAME;
            embeddingModelName = EmbeddingModelConfiguration.MICROSOFT_FOUNDRY_MODEL_NAME;
        }
        if (microsoftFoundryApiKey == null || microsoftFoundryApiKey.isBlank()) {
            openaiApiKeyMasked = "N/A (using local Ollama instance)";
        } else {
            openaiApiKeyMasked = "***********";
        }
        if (azureSearchEndpoint != null && !azureSearchEndpoint.isBlank()) {
            vectorStore = "Azure AI Search";
        } else {
            vectorStore = "Elasticsearch";
        }
        model.addAttribute("openaiBaseUrl", openaiBaseUrl);
        model.addAttribute("openaiApiKeyMasked", openaiApiKeyMasked);
        model.addAttribute("chatModelName", chatModelName);
        model.addAttribute("imageModelName", imageModelName);
        model.addAttribute("embeddingModelName", embeddingModelName);
        model.addAttribute("vectorStore", vectorStore);
        return "home";
    }
}

