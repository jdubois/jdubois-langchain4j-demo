package com.example.demo.dto;

import java.util.List;

public record Order(Integer id, List<OrderItems> items) {
}
