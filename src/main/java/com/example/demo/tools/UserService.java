package com.example.demo.tools;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Value("${PIZZA_USER_ID:}")
    private String pizzaUserId;

    @Tool("Get the current user id")
    String getUserId() {
        return pizzaUserId;
    }
}
