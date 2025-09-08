package com.example.demo.model;

import java.util.List;

public record Recipe(String title, List<Item> items) { }
