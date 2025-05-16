package com.example.demo.configuration.chatmodel.ollama;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class OllamaChatModelConfiguration {

    @Profile({"small"})
    @Bean
    ChatModel tinyllamaChatModel() {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434/")
                .modelName("tinyllama")
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Profile({"good", "elasticsearch"})
    @Bean
    ChatModel phiChatModel() {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434/")
                .modelName("phi4")
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
