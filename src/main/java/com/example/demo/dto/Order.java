package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record Order(
        Long id,
        String userId,
        LocalDateTime orderDate,
        String status,
        @JsonProperty("items") OrderItem[] items,
        double totalPrice
) {
}
