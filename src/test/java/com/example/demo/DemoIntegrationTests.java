package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=small")
@AutoConfigureMockMvc
@Testcontainers
public class DemoIntegrationTests {

    @SuppressWarnings("resource")
    @Container
    public static ComposeContainer environment =
            new ComposeContainer(new File("src/test/resources/docker-compose-test.yml"))
                    .withExposedService("qdrant-1",  6334, Wait.forListeningPort())
                    .waitingFor("ollama-1", Wait.forSuccessfulCommand("ollama pull tinyllama").withStartupTimeout(Duration.ofMinutes(5)));

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("demo")));
    }

    @Test
    void leonardoPaintedTheMonaLisa() throws Exception {
        this.mockMvc.perform(get("/2")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Leonardo da Vinci")));
    }

    @Test
    void easyRag() throws Exception {
        // Ingest data
        this.mockMvc.perform(get("/10")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")));

        // Query data
        this.mockMvc.perform(get("/11")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("120,000")));
    }
}
