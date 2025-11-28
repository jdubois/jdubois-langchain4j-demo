package com.example.demo.controller;

import com.example.demo.service.ModelsDiscoveryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the home page.
 */
@Controller
public class HomeController {

    @Value("${AZURE_SEARCH_ENDPOINT:}")
    private String azureSearchEndpoint;

    @Value("${AZURE_SEARCH_KEY:}")
    private String azureSearchKey;

    private final ModelsDiscoveryService modelsDiscoveryService;

    public HomeController(ModelsDiscoveryService modelsDiscoveryService) {
        this.modelsDiscoveryService = modelsDiscoveryService;
    }

    @GetMapping("/")
    public String home(Model model) {
        String openAiBaseUrl = this.modelsDiscoveryService.getOpenAiBaseUrl();
        String openAiApiKey = this.modelsDiscoveryService.getOpenAiApiKey();
        if (openAiBaseUrl.toLowerCase().contains("localhost")) {
            String warning = "Warning: you are using a local model, so the following demos will not work as expected: Image Generation, Function Calling, MCP, and Agentic";
            model.addAttribute("warning", warning);
        }
        String openaiApiKeyMasked;
        if (openAiApiKey == null || openAiApiKey.isBlank()) {
            openaiApiKeyMasked = "N/A";
        } else {
            openaiApiKeyMasked = "***********";
        }
        if (azureSearchEndpoint != null && !azureSearchEndpoint.isBlank()) {
            model.addAttribute("vectorStore", "Azure AI Search");
            model.addAttribute("azureSearchEndpoint", azureSearchEndpoint);
        } else {
            model.addAttribute("vectorStore", "Elasticsearch");
            model.addAttribute("azureSearchEndpoint", "N/A");

        }
        if (azureSearchKey != null && !azureSearchKey.isBlank()) {
            model.addAttribute("azureSearchKeyMasked", "***********");
        } else {
            model.addAttribute("azureSearchKeyMasked", "N/A");
        }
        model.addAttribute("openaiBaseUrl", openAiBaseUrl);
        model.addAttribute("openaiApiKeyMasked", openaiApiKeyMasked);
        model.addAttribute("chatModelName", this.modelsDiscoveryService.getChatModelName());
        model.addAttribute("imageModelName", this.modelsDiscoveryService.getImageModelName());
        model.addAttribute("embeddingModelName",  this.modelsDiscoveryService.getEmbeddingModelName());
        return "home";
    }
}

