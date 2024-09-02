package io.github.jdubois.langchain4j.demo.configuration.local;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("local")
public class LocalChatModelConfiguration {

    @Bean
    ChatLanguageModel ollamaAIChatLanguageModel() {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434/")
                .modelName("phi3")
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
