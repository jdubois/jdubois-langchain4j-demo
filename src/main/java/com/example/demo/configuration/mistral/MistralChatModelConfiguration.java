package com.example.demo.configuration.mistral;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.mistralai.MistralAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("mistral")
public class MistralChatModelConfiguration {

    @Bean
    ChatLanguageModel mistralAIChatLanguageModel() {
        return MistralAiChatModel.builder()
                .baseUrl("http://localhost:1234/v1/")
                .apiKey("foo")
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
