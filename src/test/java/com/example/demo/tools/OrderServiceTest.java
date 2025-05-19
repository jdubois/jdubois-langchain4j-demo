package com.example.demo.tools;

import com.example.demo.dto.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Test
    void placeOrder() {
        Order order = new Order();
        String result = orderService.orderPizza();
        assertNotNull(result);
    }

    @Test
    void listOrders() {
        Order[] orders = orderService.listOrders();
        assertNotNull(orders);
    }
}
