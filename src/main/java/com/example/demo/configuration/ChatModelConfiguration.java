package com.example.demo.configuration;

import com.example.demo.service.ModelsDiscoveryService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import java.util.Set;
import java.util.logging.Logger;

import static dev.langchain4j.model.chat.Capability.RESPONSE_FORMAT_JSON_SCHEMA;

@Configuration
public class ChatModelConfiguration {

    private final Logger log = Logger.getLogger(ChatModelConfiguration.class.getName());

    @Bean
    ChatModel openAIChatModel(ModelsDiscoveryService modelsDiscoveryService) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ChatModel chatModel = OpenAiOfficialChatModel.builder()
                .baseUrl(modelsDiscoveryService.getOpenAiBaseUrl())
                .apiKey(modelsDiscoveryService.getOpenAiApiKey())
                .modelName(modelsDiscoveryService.getChatModelName())
                .supportedCapabilities(Set.of(RESPONSE_FORMAT_JSON_SCHEMA))
                .strictJsonSchema(true)
                .strictTools(true)
                .build();
        stopWatch.stop();
        log.info(String.format("OpenAI Chat Model created in %.3f seconds", stopWatch.getTotalTimeSeconds()));
        return chatModel;
    }
}
