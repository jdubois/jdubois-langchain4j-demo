package com.example.demo;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.openai.models.ChatModel.GPT_4_1;

@Configuration
public class ChatModelConfiguration {

    @Value("${AZURE_OPENAI_ENDPOINT}")
    private String azureOpenAiEndpoint;

    @Value("${AZURE_OPENAI_KEY:xxx}")
    private String azureOpenAiKey;

    @Bean
    ChatModel chatModel() {
        return OpenAiOfficialChatModel.builder()
                .baseUrl("http://localhost:11434/v1")
                .apiKey("xxx")
                .modelName("phi4")
                .build();
    }
}
