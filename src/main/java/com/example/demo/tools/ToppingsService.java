package com.example.demo.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

@Service
public class ToppingsService {

    @Tool("List of pizza toppings")
    String listToppings() {
        return "Ham, Mushrooms";
    }
}
