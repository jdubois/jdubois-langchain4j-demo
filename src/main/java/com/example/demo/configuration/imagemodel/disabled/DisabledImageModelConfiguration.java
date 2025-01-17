package com.example.demo.configuration.imagemodel.disabled;

import dev.langchain4j.model.image.DisabledImageModel;
import dev.langchain4j.model.image.ImageModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!azure")
public class DisabledImageModelConfiguration {

    @Bean
    ImageModel imageModel() {
        // There is no support in LangChain4j for local image models at the moment.
        return new DisabledImageModel();
    }
}
