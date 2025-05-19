package com.example.demo.tools;

import com.example.demo.dto.Order;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static com.example.demo.tools.PizzaService.BASE_URL;

@Service
public class OrderService {

    @Value("${PIZZA_USER_ID:}")
    private String pizzaUserId;

    private final RestClient restClient;

    public OrderService() {
        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    @Tool("Order a pizza")
    String orderPizza(@P("Order containing the Pizza Id and its quantity") Order order) {
        return restClient.post()
                .uri("/orders")
                .body(order)
                .retrieve()
                .toString();
    }

    @Tool("List current pizza orders")
    Order[] listOrders() {
        return restClient.get()
                .uri("/orders?userId=" + pizzaUserId)
                .retrieve()
                .body(Order[].class);
    }
}
