package com.example.demo.configuration;

import dev.langchain4j.model.image.DisabledImageModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialImageModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import static com.openai.models.ChatModel.GPT_5_MINI;
import static com.openai.models.images.ImageModel.DALL_E_3;

@Configuration
public class ImageModelConfiguration extends AbstractModelConfiguration {

    public static final String MICROSOFT_FOUNDRY_MODEL_NAME = DALL_E_3.asString();
    public static final String OLLAMA_MODEL_NAME = "";

    @Bean
    ImageModel imageModel() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        ImageModel imageModel;
        if (modelName.equals(getOllamaModelName())) {
            imageModel = new DisabledImageModel();
        } else {
            imageModel = OpenAiOfficialImageModel.builder()
                    .baseUrl(endpoint)
                    .apiKey(apiKey)
                    .modelName(modelName)
                    .build();
        }
        log.info(String.format("OpenAI Image Model created in %.3f seconds", stopWatch.getTotalTimeSeconds()));
        return imageModel;
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
