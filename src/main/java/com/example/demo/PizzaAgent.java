package com.example.demo;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface PizzaAgent {

    @SystemMessage("The getMenu is the list of available pizzas and the list of available pizza toppings")
    @UserMessage("Get the getMenu")
    String getMenu();

    @UserMessage("Place a pizza order")
    String placeOrder();
}
