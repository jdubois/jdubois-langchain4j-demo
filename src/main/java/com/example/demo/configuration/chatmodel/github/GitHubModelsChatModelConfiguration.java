package com.example.demo.configuration.chatmodel.github;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static com.openai.models.ChatModel.GPT_4O_MINI;

@Configuration
@Profile("github")
public class GitHubModelsChatModelConfiguration {

    @Bean
    ChatModel gitHubModelsChatModel() {
        return OpenAiOfficialChatModel.builder()
                .isGitHubModels(true)
                .modelName(GPT_4O_MINI)
                .build();
    }
}
