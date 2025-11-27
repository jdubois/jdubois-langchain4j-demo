package com.example.demo.configuration;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import java.util.Set;

import static com.openai.models.ChatModel.GPT_5_MINI;
import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;

@Configuration
public class ChatModelConfiguration extends AbstractModelConfiguration {

    public static final String MICROSOFT_FOUNDRY_MODEL_NAME = GPT_5_MINI.asString();
    public static final String OLLAMA_MODEL_NAME = "llama3.2:1b";

    @Bean
    ChatModel openAIChatModel() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ChatModel chatModel = OpenAiOfficialChatModel.builder()
                .baseUrl(endpoint)
                .apiKey(apiKey)
                .modelName(modelName)
                .supportedCapabilities(Set.of(RESPONSE_FORMAT_JSON_SCHEMA))
                .strictJsonSchema(true)
                .strictTools(true)
                .build();
        stopWatch.stop();
        log.info(String.format("OpenAI Chat Model created in %.3f seconds", stopWatch.getTotalTimeSeconds()));
        return chatModel;
    }

    @Override
    String getMicrosoftFoundryModelName() {
        return MICROSOFT_FOUNDRY_MODEL_NAME;
    }

    @Override
    String getOllamaModelName() {
        return OLLAMA_MODEL_NAME;
    }
}
