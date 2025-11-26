package com.example.demo;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.junit.jupiter.api.BeforeEach;
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
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class DemoIntegrationTests {

    @SuppressWarnings("resource")
    @Container
    public static ComposeContainer environment =
            new ComposeContainer(new File("src/test/resources/docker-compose-test.yml"))
                    .withExposedService("elasticsearch-1",  9200, Wait.forListeningPort())
                    .waitingFor("ollama-1", Wait.forSuccessfulCommand("ollama pull nomic-embed-text && ollama pull llama3.2:1b").withStartupTimeout(Duration.ofMinutes(5)));

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestClient restClient;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

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
        this.mockMvc.perform(get("/9")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")));

        // Refresh the index so the data is visible
        restClient.performRequest(new Request("POST", "/default/_refresh"));

        // Query data
        this.mockMvc.perform(get("/10")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("125,000")));
    }
}
