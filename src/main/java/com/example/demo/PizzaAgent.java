package com.example.demo;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

import static com.example.demo.tools.PizzaService.BASE_URL;

public interface PizzaAgent {

    @SystemMessage("To create the menu, you need the list of available pizzas and the list of available pizza toppings")
    @UserMessage("""
        Create a menu for a restaurant in HTML format.
        This is a code snippet that will be inserted into a Bootstrap page. 
        Make it extra fancy. 
        We need all the available pizzas and all the available toppings.
               Do not put HTML headers tags, or instructions on how to use the code, it will be inserted in a Boostrap page that already has the headers.
        """ +
        "The images have a base URL of \"" + BASE_URL + "\\images\" that you need to append before each image URL.\n"
    )
    String getMenu();

    @UserMessage("Order one random pizza from the menu, with one random toppings from the menu")
    String placeOrder();

    @UserMessage("List the current pizza orders in HTML format")
    String listOrders();
}
