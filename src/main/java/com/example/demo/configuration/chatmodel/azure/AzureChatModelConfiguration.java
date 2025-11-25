package com.example.demo.configuration.chatmodel.azure;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Set;

import static com.openai.models.ChatModel.GPT_5_MINI;
import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;

@Configuration
@Profile("azure")
public class AzureChatModelConfiguration {

    @Value("${OPENAI_BASE_URL}")
    private String microsoftFoundryEndpoint;

    @Value("${OPENAI_API_KEY}")
    private String microsoftFoundryApiKey;

    @Bean
    ChatModel azureOpenAIChatModel() {
        return OpenAiOfficialChatModel.builder()
                .baseUrl(microsoftFoundryEndpoint)
                .apiKey(microsoftFoundryApiKey)
                .modelName(GPT_5_MINI)
                .supportedCapabilities(Set.of(RESPONSE_FORMAT_JSON_SCHEMA))
                .strictJsonSchema(true)
                .strictTools(true)
                .build();
    }
}
