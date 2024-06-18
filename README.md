# LangChain4J demo

_Author: [Julien Dubois](https://www.julien-dubois.com)_

## Goal

This is a Spring Boot project that demonstrates how to use LangChain4J to create Java applications using LLMs.

It contains the following demos:

- How to generate an image using Dalle-3.
- How to generate a text using GPT-4o and Phi-3.
- How to use a chat conversation with memory of the context.
- How to ingest data into a vector database, and use it.

Those demos either run in the cloud (using Azure, with Azure OpenAI and Azure AI Search), or locally (with Docker, using Ollama + Phi-3 and Qdrant).

## Configuration

### Running in the cloud with Azure

Azure is enabled by using the `azure` Spring Boot profile.
One way to do this is to set `spring.profiles.active=azure` in the `src/main/resources/application.properties` file.

To provision the Azure resources, you need to run the `src/main/script/deploy-azure-openai-models.sh` script. It will create the following resources:

- An Azure OpenAI instance, secured using Entra ID, with the necessary OpenAI models for this demo.
- An Azure AI Search instance.

At the end of this script, the following environment variables will be displayed (and stored in the `.env` file), and you will need them to run the application:
- `AZURE_OPENAI_ENDPOINT`: your Azure OpenAI URL endpoint.
- `AZURE_SEARCH_ENDPOINT`: your Azure AI Search URL endpoint.
- `AZURE_SEARCH_KEY`: your Azure AI Search API key.

### Running locally with Docker

Local execution is enabled by using the `local` Spring Boot profile.
One way to do this is to set `spring.profiles.active=local` in the `src/main/resources/application.properties` file.

To set up the necessary resources, you need to have Docker installed on your machine, and run with Docker Compose the `src/main/docker/docker-compose.yml` file.

It will set up:

- An Ollama instance, with the Phi-3 and the nomic-embed-text models. Its Web UI is available at `http://localhost:8081/`.
- A Qdrant instance. Its Web UI is available at `http://localhost:6333/dashboard`.

## Running the demos

Once the resources (Azure or local) are configured, you can run the demos using the following command:

```shell
./mvnw spring-boot:run
```

Then you can access the base URL, where you find the Web UI:

```
http://localhost:8080/
```
