# LangChain4J demo

_Author: [Julien Dubois](https://www.julien-dubois.com)_

## Video

This demo is supporting a video called "LangChain4J - use the power of LLMs in Java!", available at [https://youtu.be/x8kkjmCZTaw](https://youtu.be/x8kkjmCZTaw).

## Goal

This is a Spring Boot project that demonstrates how to use LangChain4J to create Java applications using LLMs.

It contains 7 demos:

- How to generate an image using Dalle-3.
- How to generate a text using GPT-4 and Mistral 7B.
- How to use a chat conversation with memory of the context.
- How to ingest data into a vector database, and use it.

## Configuration

### Azure OpenAI

You need to set the following environment variables:

- `AZURE_OPENAI_ENDPOINT`: your Azure OpenAI URL endpoint.
- `AZURE_OPENAI_API_KEY`: your Azure OpenAI API key.

### MistralAI

You can use a local version of Mistral 7B, and you need to set following environment variables:

- `MISTRAL_AI_BASE_URL`: your MistralAI URL endpoint (for example `http://localhost:1234/v1/` when running locally).
- `MISTRAL_AI_KEY`: your MistralAI API key (which you would not use when running the model locally, so you can use `foo` as a value).

### Azure AI Search

You need to set the following environment variables:

- `AZURE_SEARCH_ENDPOINT`: your Azure AI Search URL endpoint.
- `AZURE_SEARCH_KEY`: your Azure AI Search API key.

## Running the demos

You can run the demos using the following command:

```shell
./mvnw spring-boot:run
```

Then you can access the base URL, where you find the Web UI:

```
http://localhost:8080/
```
