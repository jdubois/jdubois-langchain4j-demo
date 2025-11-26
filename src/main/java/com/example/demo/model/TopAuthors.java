package com.example.demo.model;

import java.util.List;
import java.util.stream.Collectors;

public record TopAuthors(List<Author> authors) {

    @Override
    public String toString() {
        String authorsJson = authors.stream()
                .map(Author::toString)
                .collect(Collectors.joining(",", "[", "]"));
        return "{\"authors\":" + authorsJson + "}";
    }
}
