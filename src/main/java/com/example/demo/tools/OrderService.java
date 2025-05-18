package com.example.demo.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Tool("Order a pizza")
    String orderPizza(@P("Name of the pizza") String name) {
        return "";
    }

    @Tool("List current pizza orders")
    String listPizzaOrders() {
        return "";
    }
}
