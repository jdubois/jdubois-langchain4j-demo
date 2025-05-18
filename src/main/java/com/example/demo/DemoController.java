package com.example.demo;

import com.example.demo.tools.OrderService;
import com.example.demo.tools.PizzaService;
import com.example.demo.tools.ToppingsService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    PizzaAgent pizzaAgent;

    public DemoController(ChatModel chatModel, PizzaService pizzaService, ToppingsService toppingsService, OrderService orderService) {
        pizzaAgent = AiServices.builder(PizzaAgent.class)
                .chatModel(chatModel)
                .tools(pizzaService, toppingsService, orderService)
                .build();
    }

    @GetMapping("/")
    public String demo() {
        return "demo";
    }

    @GetMapping("/1")
    String getMenu(Model model) {
        String answer = pizzaAgent.getMenu();
        return getView(model, "1: simple question", "Available pizzas", answer);
    }

    @GetMapping("/2")
    String placeOrder(Model model) {
        String answer = pizzaAgent.placeOrder();
        return getView(model, "1: simple question", "Place order", answer);
    }

    @GetMapping("/3")
    String placeOrder(Model model) {
        String answer = pizzaAgent.listOrders();
        return getView(model, "1: simple question", "List pizza orders", answer);
    }

    private static String getView(Model model, String demoName, String question, String answer) {
        model.addAttribute("demo", demoName);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }
}
