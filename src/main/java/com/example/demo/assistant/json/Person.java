package com.example.demo.assistant.json;

import java.util.List;

public class Person {
    private final String name;
    private final List<String> favouriteColors;

    public Person(String name, List<String> favouriteColors) {
        this.name = name;
        this.favouriteColors = favouriteColors;
    }

    public String getName() {
        return name;
    }

    public List<String> getFavouriteColors() {
        return favouriteColors;
    }
}
