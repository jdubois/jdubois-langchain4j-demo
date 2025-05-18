package com.example.demo.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

@Service
public class PizzaService {

    @Tool("List of available pizzas")
    String listPizzas() {
        return "Margarita, Regina, Calzone";
    }
}
