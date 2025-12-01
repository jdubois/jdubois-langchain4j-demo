package com.example.demo;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Fail.fail;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class DemoIntegrationTests {

    private static final Logger log = Logger.getLogger(DemoIntegrationTests.class.getName());

    @SuppressWarnings("resource")
    @Container
    public static ComposeContainer environment =
            new ComposeContainer(new File("src/test/resources/docker-compose-test.yml"))
                    .withExposedService("elasticsearch",  9200,
                            Wait.forListeningPort());

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RestClient restClient;

    private MockMvcTester mockMvc;


    @BeforeAll
    public static void init() {
        String openaiBaseUrl = System.getenv("OPENAI_BASE_URL");
        String openaiApiKey = System.getenv("OPENAI_API_KEY");

        boolean hasVariables = openaiBaseUrl != null && !openaiBaseUrl.isBlank()
                && openaiApiKey != null && !openaiApiKey.isBlank();

        if (!hasVariables) {
            log.severe("Integration tests are disabled: Required environment variables OPENAI_BASE_URL and/or OPENAI_API_KEY are not set or are blank. " +
                    "Please configure these environment variables to run the integration tests.");

            fail("Required environment variables OPENAI_BASE_URL and/or OPENAI_API_KEY are not set or are blank.");
        }
    }

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcTester.from(this.webApplicationContext);
    }

    @Test
    void shouldReturnDefaultMessage() {
        mockMvc.get().uri("/")
                .assertThat()
                .hasStatusOk()
                .bodyText().contains("demo");
    }

    @Test
    void leonardoPaintedTheMonaLisa() {
        mockMvc.get().uri("/2")
                .assertThat()
                .hasStatusOk()
                .bodyText().contains("Leonardo da Vinci");
    }

    @Test
    void reasoningQuestionAboutMariasFourthDaughter() {
        mockMvc.get().uri("/3")
                .assertThat()
                .hasStatusOk()
                // Expect the model to reason that the fourth daughter is Maria
                .bodyText().contains("Maria");
    }

    @Test
    void advancedQuestionIsAnsweredInFrench() {
        mockMvc.get().uri("/4")
                .assertThat()
                .hasStatusOk()
                // We loosely check for French-specific words to avoid brittle assertions
                .bodyText().contains("Mona Lisa");
    }

    @Test
    void questionWithoutMemory() {
        mockMvc.get().uri("/5")
                .assertThat()
                .hasStatusOk();
    }

    @Test
    void questionWithMemory() {
        mockMvc.get().uri("/6")
                .assertThat()
                .hasStatusOk()
                // Expect similar answer mentioning the location
                .bodyText().contains("Louvre");
    }

    @Test
    void simpleVectorDatabaseIngestion() {
        mockMvc.get().uri("/7")
                .assertThat()
                .hasStatusOk()
                .bodyText().contains("7: Simple data ingestion", "OK");
    }

    @Test
    void querySimpleVectorDatabase() throws IOException {
        // Ensure data is ingested first
        mockMvc.get().uri("/7")
                .assertThat()
                .hasStatusOk();

        // Refresh the index so the data is visible
        Response response = restClient.performRequest(new Request("POST", "/default/_refresh"));
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);

        mockMvc.get().uri("/8")
                .assertThat()
                .hasStatusOk()
                // Expect fruit-related contents from the in-memory store
                .bodyText().contains("banana", "apple", "strawberry");
    }

    @Test
    void easyRag() throws IOException {
        // Ingest data
        mockMvc.get().uri("/9")
                .assertThat()
                .hasStatusOk()
                .bodyText().contains("OK");

        // Refresh the index so the data is visible
        Response response = restClient.performRequest(new Request("POST", "/default/_refresh"));
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(200);

        // Query data
        mockMvc.get().uri("/10")
                .assertThat()
                .hasStatusOk()
                .bodyText().contains("125,000");
    }

    @Test
    void structuredOutputsReturnRecipe() {
        mockMvc.get().uri("/11")
                .assertThat()
                .hasStatusOk()
                .bodyText().contains("11: Structured Outputs", "apple", "ingredients");
    }

    @Test
    void functionCallingCreatesGistRecipe() {
        mockMvc.get().uri("/12")
                .assertThat()
                .hasStatusOk()
                .bodyText().contains("12: Function calling", "apple", "ingredients");
    }

    @Test
    void multipleToolsAndStructuredOutputsProduceMarkdownRecipe() {
        mockMvc.get().uri("/13")
                .assertThat()
                .hasStatusOk()
                .bodyText().contains("13: Multiple tools and structured outputs", "apple", "ingredients");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "GITHUB_TOKEN", matches = ".+")
    void mcpGitHubReturnsTopAuthors() {
        mockMvc.get().uri("/14")
                .assertThat()
                .hasStatusOk()
                .bodyText().contains("14: Using an MCP Server", "langchain4j/langchain4j");
    }

    @Test
    @EnabledIfEnvironmentVariable(named = "GITHUB_TOKEN", matches = ".+")
    void agenticDemoProducesGistUrl() {
        mockMvc.get().uri("/15")
                .assertThat()
                .hasStatusOk()
                .bodyText().contains("15: Agents working together", "gist");
    }
}
