package com.example.demo;

import com.openai.models.ChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatModelConfiguration {

    @Value("${AZURE_OPENAI_ENDPOINT}")
    private String azureOpenAiEndpoint;

    @Value("${AZURE_OPENAI_KEY:}")
    private String azureOpenAiKey;

    @Bean
    ChatLanguageModel azureOpenAIChatLanguageModel() {
        return OpenAiOfficialChatModel.builder()
                .baseUrl(azureOpenAiEndpoint)
                .apiKey(azureOpenAiKey)
                .modelName(ChatModel.GPT_4_1)
                .build();
    }
}
