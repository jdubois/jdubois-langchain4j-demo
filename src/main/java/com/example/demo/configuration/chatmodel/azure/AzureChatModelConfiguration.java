package com.example.demo.configuration.chatmodel.azure;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatModel;
import dev.langchain4j.model.output.TokenUsage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    ChatModel azureOpenAIChatModel() {
        List<ChatModelListener> listeners = new ArrayList<>();
        ChatModelListener listener = new ChatModelListener() {

            @Override
            public void onResponse(ChatModelResponseContext responseContext) {
                TokenUsage tokenUsage = responseContext.chatResponse().tokenUsage();
                int messageNumber = responseContext.chatRequest().messages().size();
                System.out.println("Message number: " + messageNumber);
                System.out.println("Finish reason: " + responseContext.chatResponse().finishReason().name());
            }
        };
        listeners.add(listener);

        return OpenAiOfficialChatModel.builder()
                .baseUrl(azureOpenAiEndpoint)
                .apiKey(azureOpenAiKey)
                .modelName(GPT_4O_MINI)
                .supportedCapabilities(Set.of(RESPONSE_FORMAT_JSON_SCHEMA))
                .strictJsonSchema(true)
                .strictTools(true)
                .listeners(listeners)
                .build();
    }
}
