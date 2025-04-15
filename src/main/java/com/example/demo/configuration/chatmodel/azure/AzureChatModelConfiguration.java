package com.example.demo.configuration.chatmodel.azure;

import com.openai.models.ChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Set;

import static com.openai.models.ChatModel.GPT_4O;
import static com.openai.models.ChatModel.GPT_4O_MINI;
import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;

@Configuration
@Profile("azure")
public class AzureChatModelConfiguration {

    @Value("${AZURE_OPENAI_ENDPOINT}")
    private String azureOpenAiEndpoint;

    @Value("${AZURE_OPENAI_KEY}")
    private String azureOpenAiKey;

    @Bean
    ChatLanguageModel azureOpenAIChatLanguageModel() {
        return OpenAiOfficialChatModel.builder()
                .baseUrl(azureOpenAiEndpoint)
                //.apiKey(azureOpenAiKey)
                .modelName(ChatModel.GPT_4_1)
                .supportedCapabilities(Set.of(RESPONSE_FORMAT_JSON_SCHEMA))
                .strictJsonSchema(true)
                .strictTools(true)
                .build();
    }
}
