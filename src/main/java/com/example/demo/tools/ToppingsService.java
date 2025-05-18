package com.example.demo.tools;

import com.example.demo.dto.Toppings;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class ToppingsService {

    private static final String BASE_URL = "https://www.julien-dubois.com";

    private final RestClient restClient;

    public ToppingsService() {
        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    @Tool("List of available pizza toppings")
    Toppings[] listToppings() {
        return restClient.get()
                .uri("/toppings")
                .retrieve()
                .body(Toppings[].class);
    }
}
