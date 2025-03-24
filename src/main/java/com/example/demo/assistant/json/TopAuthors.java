package com.example.demo.assistant.json;

import java.util.List;

public record TopAuthors(List<Author> authors) {

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        authors
                .forEach(author -> result
                        .append(author.firstName())
                        .append(" ")
                        .append(author.lastName())
                        .append(" ")
                        .append(author.NumberOfCommits())
                        .append(" | "));
        return result.toString();
    }
}
