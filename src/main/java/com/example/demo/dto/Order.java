package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public record Order(
        Long id,
        String userId,
        LocalDateTime orderDate,
        String status,
        List<OrderItem> orderItems,
        double totalPrice
) {
}
