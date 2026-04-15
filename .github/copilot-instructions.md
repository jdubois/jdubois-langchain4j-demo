# Copilot Instructions

## Build & Run

- **Java 25+** required. Uses Maven wrapper (`./mvnw`).
- **Build & test:** `./mvnw -B verify`
- **Run app:** `./mvnw spring-boot:run` (serves at http://localhost:8080)
- **Run a single test:** `./mvnw test -Dtest=DemoIntegrationTests#leonardoPaintedTheMonaLisa`
- **Native image:** `./mvnw -Pnative native:compile` (tests are skipped automatically)

Tests require `OPENAI_BASE_URL` and `OPENAI_API_KEY` environment variables, plus a running Elasticsearch container (managed via TestContainers from `src/test/resources/docker-compose-test.yml`). Tests tagged `github-dependent` additionally require `GITHUB_TOKEN`.

## Architecture

This is a Spring Boot 4 + LangChain4j 1.11 demo application with 15 progressive demos showcasing LLM capabilities: text/image generation, chat memory, RAG, structured outputs, function calling, MCP integration, and multi-agent orchestration.

### Cloud/Local dual configuration

There are no Spring profiles. All configuration switches on environment variables:

- If `OPENAI_BASE_URL` and `OPENAI_API_KEY` are set → cloud mode (Microsoft Foundry / OpenAI-compatible)
- Otherwise → local mode (Ollama + Elasticsearch via Docker Compose at `src/main/docker/docker-compose.yml`)
- If `AZURE_SEARCH_ENDPOINT` and `AZURE_SEARCH_KEY` are set → Azure AI Search; otherwise → Elasticsearch

`ModelsDiscoveryService` centralizes this resolution logic. The `.env` file at the project root stores credentials (never committed).

### MVC layer

Controllers are template-based (`@Controller` + Mustache), not REST. Each demo is a numbered endpoint (`/1` through `/15`). Templates use Bootstrap 5 with a shared layout via Mustache partials (`partials/header.mustache`, `partials/nav.mustache`, `partials/footer.mustache`).

### LangChain4j patterns

- **AI Service interfaces** — Declarative interfaces built via `AiServices.builder()` at call time. Examples: `ApplePieService` (structured output), `RagAssistant` (RAG), `GitHubMcpService` (MCP tools).
- **Tool classes** — Spring beans with `@Tool`-annotated methods and `@P` for parameter docs. Examples: `GistService`, `MarkdownService`, `ListCreationTool`.
- **Agent interfaces** — Decorated with `@Agent`, `@UserMessage`, `@UserField`. Built via `AgenticServices.agentBuilder()`. Orchestrated with `sequenceBuilder()` (sequential) or `parallelBuilder()` (parallel). See `AgenticController` for the full 5-agent orchestration pattern.
- **MCP integration** — `McpToolConfiguration` configures a stdio-based MCP client (GitHub MCP Server) that provides a `ToolProvider` bean.

### Package layout

```
com.example.demo
├── configuration/   # Spring @Configuration beans (models, embedding store, MCP, native hints)
├── controller/      # MVC controllers, one per demo category
├── model/           # Java records for structured LLM outputs (Recipe, Author, Item, TopAuthors)
└── service/
    ├── agent/       # LangChain4j agent interfaces + tools for agentic demo
    ├── mcp/         # MCP-based AI service interface
    ├── rag/         # RAG assistant AI service interface
    └── tool/        # @Tool-annotated service beans for function calling
```

## Key Conventions

- **Virtual threads** are enabled (`spring.threads.virtual.enabled=true`).
- **Lazy initialization** is on for startup performance (`spring.main.lazy-initialization=true`).
- Model classes are **Java records** with custom `toString()` for JSON-friendly output.
- AI Service and Agent interfaces are **instantiated per-request** inside controller methods (not singleton beans), because they are configured with request-specific parameters like chat memory.
- GraalVM native image support requires `NativeRuntimeHintsConfiguration` — when adding new classes that need reflection (e.g., Jackson deserialization targets), register them there.
- Logging uses `logback-spring.xml` (not `application.properties`) for GraalVM native image compatibility.
