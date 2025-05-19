package com.example.demo.tools;

import com.example.demo.dto.Toppings;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static com.example.demo.tools.PizzaService.BASE_URL;

@Service
public class ToppingsService {

    private final RestClient restClient;

    public ToppingsService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Tool("List of available pizza toppings")
    Toppings[] listToppings() {
        return restClient.get()
                .uri(BASE_URL + "/toppings")
                .retrieve()
                .body(Toppings[].class);
    }
}
