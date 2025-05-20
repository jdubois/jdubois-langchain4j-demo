package com.example.demo.tools;

import com.example.demo.dto.Order;
import com.example.demo.dto.OrderItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Value("${PIZZA_USER_ID:}")
    private String pizzaUserId;

    @Test
    void placeOrder() {
        Order order = new Order(null, pizzaUserId, null, "PENDING", new OrderItem[]{ new OrderItem("1", 1)}, 0.0);
        Order result = orderService.orderPizza(order);
        assertNotNull(result);

        // Check if the order was placed successfully by calling orderService.listOrders()
        Order[] orders = orderService.listOrders();
        assertNotNull(orders);
        assertTrue(orders.length > 0);

        // Find the order we just created
        boolean orderFound = Arrays.stream(orders)
                .anyMatch(o -> o.id().equals(result.id()));
        assertTrue(orderFound, "The created order should be found in the list of orders");

    }

    @Test
    void listOrders() {
        Order[] orders = orderService.listOrders();
        assertNotNull(orders);
    }
}
