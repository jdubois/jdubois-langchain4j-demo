package com.example.demo.configuration.local;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class LocalChatModelConfiguration {

    @Bean
    @Profile("local")
    ChatLanguageModel ollamaAIChatLanguageModel() {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434/")
                .modelName("phi3")
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    @Profile("disabled")
    ChatLanguageModel mistralAIChatLanguageModel() {
        return MistralAiChatModel.builder()
                .baseUrl("http://localhost:1234/v1/")
                .apiKey("foo")
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
