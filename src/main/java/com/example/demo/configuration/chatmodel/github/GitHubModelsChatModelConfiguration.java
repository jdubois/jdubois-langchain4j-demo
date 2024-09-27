package com.example.demo.configuration.chatmodel.github;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.github.GitHubModelsChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static dev.langchain4j.model.github.GitHubModelsChatModelName.GPT_4_O_MINI;

@Configuration
@Profile("github")
public class GitHubModelsChatModelConfiguration {

    @Value("${GITHUB_TOKEN}")
    private String gitHubToken;

    @Bean
    ChatLanguageModel gitHubModelsChatLanguageModel() {
        return GitHubModelsChatModel.builder()
                .gitHubToken(gitHubToken)
                .modelName(GPT_4_O_MINI)
                .logRequestsAndResponses(true)
                .build();
    }
}
