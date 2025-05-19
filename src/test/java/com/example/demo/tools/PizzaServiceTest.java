package com.example.demo.tools;

import com.example.demo.dto.Pizza;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PizzaServiceTest {

    @Autowired
    PizzaService pizzaService;

    @Test
    void listPizzas() {
        Pizza[] pizzas = pizzaService.listPizzas();
        assertNotNull(pizzas);
    }
}
