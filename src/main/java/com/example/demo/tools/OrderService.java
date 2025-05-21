package com.example.demo.tools;

import com.example.demo.dto.Order;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import static com.example.demo.tools.PizzaService.BASE_URL;

@Service
public class OrderService {

    private final RestClient restClient;

    public OrderService(RestClient restClient) {
        this.restClient = restClient;
    }

    @Tool("Order a pizza that needs the current user id")
    Order orderPizza(@P("Order containing the user id, the Pizza Id and its quantity") Order order) {
        return restClient.post()
                .uri(BASE_URL + "/orders")
                .body(order)
                .retrieve()
                .toEntity(Order.class)
                .getBody();
    }

    @Tool("List current pizza orders for the user id")
    Order[] listOrders(@P("User id") String pizzaUserId) {
        return restClient.get()
                .uri(BASE_URL+ "/orders?userId=" + pizzaUserId)
                .retrieve()
                .body(Order[].class);
    }
}
