package com.example.demo;

import com.example.demo.tools.OrderService;
import com.example.demo.tools.PizzaService;
import com.example.demo.tools.ToppingsService;
import com.example.demo.tools.UserService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    PizzaAgent pizzaAgent;

    PizzaAgent mcpPizzaAgent;

    public DemoController(ChatModel chatModel,
                          PizzaService pizzaService,
                          ToppingsService toppingsService,
                          OrderService orderService,
                          UserService userService,
                          ToolProvider mcpToolProvider) {

        pizzaAgent = AiServices.builder(PizzaAgent.class)
                .chatModel(chatModel)
                .tools(pizzaService, toppingsService, orderService, userService)
                .build();

        mcpPizzaAgent= AiServices.builder(PizzaAgent.class)
                .chatModel(chatModel)
                .tools(userService)
                .toolProvider(mcpToolProvider)
                .build();
    }

    @GetMapping("/")
    public String demo() {
        return "demo";
    }

    @GetMapping("/1")
    String getMenu(Model model) {
        String answer = pizzaAgent.getMenu();
        return getView(model, "1: get the menu", "Here's the menu", answer);
    }

    @GetMapping("/2")
    String placeOrder(Model model) {
        String answer = pizzaAgent.placeOrder();
        return getView(model, "2: place an order", "Place order", answer);
    }

    @GetMapping("/3")
    String listOrders(Model model) {
        String answer = pizzaAgent.listOrders();
        return getView(model, "1: get the orders", "List pizza orders", answer);
    }

    @GetMapping("/4")
    String getMenuWithMcp(Model model) {
        String answer = mcpPizzaAgent.getMenu();
        return getView(model, "4: get the menu with MCP", "Here's the menu", answer);
    }

    @GetMapping("/5")
    String placeOrderWithMcp(Model model) {
        String answer = mcpPizzaAgent.placeOrder();
        return getView(model, "5: place an order with MCP", "Place order", answer);
    }

    @GetMapping("/6")
    String listOrdersWithMcp(Model model) {
        String answer = mcpPizzaAgent.listOrders();
        return getView(model, "6: get the orders with MCP", "List pizza orders", answer);
    }

    private static String getView(Model model, String demoName, String question, String answer) {
        model.addAttribute("demo", demoName);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }
}
