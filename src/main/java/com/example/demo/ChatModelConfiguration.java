package com.example.demo;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

import static com.openai.models.ChatModel.GPT_4_1;
import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;

@Configuration
public class ChatModelConfiguration {

    @Value("${AZURE_OPENAI_ENDPOINT}")
    private String azureOpenAiEndpoint;

    @Value("${AZURE_OPENAI_KEY:}")
    private String azureOpenAiKey;

    @Bean
    ChatModel chatModel() {
        return OpenAiOfficialChatModel.builder()
                .baseUrl(azureOpenAiEndpoint)
                .apiKey(azureOpenAiKey)
                .modelName(GPT_4_1)
                .supportedCapabilities(Set.of(RESPONSE_FORMAT_JSON_SCHEMA))
                .strictJsonSchema(true)
                .strictTools(true)
                .build();
    }
}
