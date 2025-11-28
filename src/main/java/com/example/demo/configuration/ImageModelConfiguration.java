package com.example.demo.configuration;

import com.example.demo.service.ModelsDiscoveryService;
import dev.langchain4j.model.image.DisabledImageModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialImageModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import java.util.logging.Logger;

@Configuration
public class ImageModelConfiguration {

    private final Logger log = Logger.getLogger(ImageModelConfiguration.class.getName());

    @Bean
    ImageModel imageModel(ModelsDiscoveryService modelsDiscoveryService) {
        String imageModelName = modelsDiscoveryService.getImageModelName();
        if (imageModelName.equals("")) {
            log.info("No Image Model configured.");
            return new DisabledImageModel();
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ImageModel imageModel = OpenAiOfficialImageModel.builder()
                .baseUrl(modelsDiscoveryService.getOpenAiBaseUrl())
                .apiKey(modelsDiscoveryService.getOpenAiApiKey())
                    .modelName(imageModelName)
                    .build();

        log.info(String.format("OpenAI Image Model created in %.3f seconds", stopWatch.getTotalTimeSeconds()));
        return imageModel;
    }
}
