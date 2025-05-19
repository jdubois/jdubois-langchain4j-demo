package com.example.demo.dto;

public record Pizza(int id, String name, String description, long price, String imageUrl, int[] toppings) {
}
