package com.example.demo.assistant.json;

import java.util.List;

public class Person {
    private String name;
    private List<String> favouriteColors;

    public Person() {
        this.name = null;
        this.favouriteColors = null;
    }

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

    public void setName(String name) {
        this.name = name;
    }

    public void setFavouriteColors(List<String> favouriteColors) {
        this.favouriteColors = favouriteColors;
    }
}
