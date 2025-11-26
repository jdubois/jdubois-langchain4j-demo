package com.example.demo.model;

public record Author(String firstName, String lastName, int NumberOfCommits) {

    @Override
    public String toString() {
        return "{\"firstName\":\"" + firstName + "\",\"lastName\":\"" + lastName + "\",\"NumberOfCommits\":" + NumberOfCommits + "}";
    }
}
