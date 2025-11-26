package com.example.demo.configuration.chatmodel.ollama;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class OllamaChatModelConfiguration {

    @Value("${ollama.port:11434}")
    private String ollamaPort;

    @Profile({"local"})
    @Bean
    ChatModel tinyllamaChatModel() {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:" + ollamaPort)
                .modelName("llama3.2:1b")
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
