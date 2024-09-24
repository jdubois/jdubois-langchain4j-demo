package com.example.demo.configuration.model.github;

import dev.langchain4j.model.image.DisabledImageModel;
import dev.langchain4j.model.image.ImageModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("github")
public class GitHubModelsImageModelConfiguration {

    @Bean
    ImageModel imageModel() {
        // There is no support in LangChain4J for local image models at the moment.
        return new DisabledImageModel();
    }
}
