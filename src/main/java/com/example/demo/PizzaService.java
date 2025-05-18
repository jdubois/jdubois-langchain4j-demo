package com.example.demo;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

@Service
public class PizzaService {

    @Tool("List of available pizzas")
    String listPizza() {
        return "Margarita, Regina, Calzone";
    }
}
