package com.example.demo;

import com.example.demo.service.ModelsDiscoveryService;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

import static com.example.demo.service.ModelsDiscoveryService.LOCAL_CHAT_MODEL;
import static com.example.demo.service.ModelsDiscoveryService.LOCAL_EMBEDDING_MODEL;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class DemoIntegrationTests {

    @SuppressWarnings("resource")
    @Container
    public static ComposeContainer environment =
            new ComposeContainer(new File("src/test/resources/docker-compose-test.yml"))
                    .withExposedService("elasticsearch-1",  9200,
                            Wait.forListeningPort())
                    .waitingFor("ollama-1",
                            Wait.forSuccessfulCommand("ollama pull " + LOCAL_CHAT_MODEL + " && ollama pull " + LOCAL_EMBEDDING_MODEL)
                                    .withStartupTimeout(Duration.ofMinutes(5)));

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ModelsDiscoveryService modelsDiscoveryService;

    @Autowired
    private RestClient restClient;

    private MockMvcTester mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcTester.from(this.webApplicationContext);
    }

    @Test
    void shouldReturnDefaultMessage() throws Exception {
        mockMvc.get().uri("/")
                .assertThat()
                .hasStatusOk()
                .bodyText().contains("demo");
    }

    @Test
    void leonardoPaintedTheMonaLisa() throws Exception {
        mockMvc.get().uri("/2")
                .assertThat()
                .hasStatusOk()
                .bodyText().contains("Leonardo da Vinci");
    }

    @Test
    void easyRag() throws Exception {
        // Ingest data
        mockMvc.get().uri("/9")
                .assertThat()
                .hasStatusOk()
                .bodyText().contains("OK");

        // Refresh the index so the data is visible
        restClient.performRequest(new Request("POST", "/default/_refresh"));

        // Query data
        mockMvc.get().uri("/10")
                .assertThat()
                .hasStatusOk()
                .bodyText().contains("125,000");
    }
}
