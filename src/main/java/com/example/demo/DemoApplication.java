package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    static void main(String[] args) {
        SpringApplication app = new SpringApplication(DemoApplication.class);
        // Startup performance optimizations
        app.setLazyInitialization(true);
        app.run(args);
    }
}
