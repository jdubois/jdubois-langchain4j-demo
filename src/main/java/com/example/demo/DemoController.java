package com.example.demo;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    PizzaAgent pizzaAgent;

    public DemoController(ChatModel chatModel, PizzaService pizzaService) {
        pizzaAgent = AiServices.builder(PizzaAgent.class)
                .chatModel(chatModel)
                .tools(pizzaService)
                .build();
    }

    @GetMapping("/")
    public String demo() {
        return "demo";
    }

    @GetMapping("/1")
    String getAnswer(Model model) {
        String answer = pizzaAgent.availablePizza();
        return getView(model, "1: simple question", "Available pizzas", answer);
    }

    private static String getView(Model model, String demoName, String question, String answer) {
        model.addAttribute("demo", demoName);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }
}
