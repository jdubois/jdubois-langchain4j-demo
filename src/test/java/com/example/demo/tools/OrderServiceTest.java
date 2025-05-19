package com.example.demo.tools;

import com.example.demo.dto.Order;
import com.example.demo.dto.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Value("${PIZZA_USER_ID:}")
    private String pizzaUserId;

    @Test
    void placeOrder() {
        Order order = new Order(null, pizzaUserId, null, "PENDING", List.of(new OrderItem(1L, 1)), 0.0);
        Order result = orderService.orderPizza(order);
        assertNotNull(result);
    }

    @Test
    void listOrders() {
        Order[] orders = orderService.listOrders();
        assertNotNull(orders);
    }
}
