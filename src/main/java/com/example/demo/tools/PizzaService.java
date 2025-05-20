package com.example.demo.tools;

import com.example.demo.dto.Pizza;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class PizzaService {

    public static final String BASE_URL = "https://func-pizza-api-vqqlxwmln5lf4.azurewebsites.net/api";

    private final RestClient restClient;

    public PizzaService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Tool("List of available pizzas")
    Pizza[] listPizzas() {
        return restClient.get()
                .uri(BASE_URL + "/pizzas")
                .retrieve()
                .body(Pizza[].class);
    }
}
