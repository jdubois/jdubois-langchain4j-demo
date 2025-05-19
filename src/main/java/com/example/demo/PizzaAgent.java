package com.example.demo;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface PizzaAgent {

    @SystemMessage("The getMenu is the list of available pizzas and the list of available pizza toppings")
    @UserMessage("Get the getMenu")
    String getMenu();

    @UserMessage("Order one pizza with random toppings")
    String placeOrder();

    @UserMessage("List the current pizza orders")
    String listOrders();
}
