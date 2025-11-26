package com.example.demo.model;

public record Item(String title, String description, boolean completed) {

    @Override
    public String toString() {
        return "{\"title\":\"" + title + "\",\"description\":\"" + description + "\",\"completed\":" + completed + "}";
    }
}
