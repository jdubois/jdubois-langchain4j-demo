package com.example.demo.model;

import java.util.List;
import java.util.stream.Collectors;

public record Recipe(String title, List<Item> items) {

    @Override
    public String toString() {
        String itemsJson = items.stream()
                .map(Item::toString)
                .collect(Collectors.joining(",", "[", "]"));
        return "{\"title\":\"" + title + "\",\"items\":" + itemsJson + "}";
    }
}
