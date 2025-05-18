package com.example.demo.tools;

import com.example.demo.dto.Pizza;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PizzaService {

    private static final String BASE_URL = "https://www.julien-dubois.com";

    private final RestClient restClient;

    public PizzaService() {
        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    @Tool("List of available pizzas")
    Pizza[] listPizzas() {
        return restClient.get()
                .uri("/pizzas")
                .retrieve()
                .body(Pizza[].class);
    }
}
