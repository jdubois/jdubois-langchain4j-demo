# LangChain4J demo

_Author: [Julien Dubois](https://www.julien-dubois.com)_

## Goal

This is a Spring Boot project that demonstrates how to use LangChain4J to create Java applications using LLMs.

It contains the following demos:

- How to generate an image using Dalle-3.
- How to generate a text using GPT-4o, GPT-4o-mini, Phi-3.5 and tinyllama.
- How to use a chat conversation with memory of the context.
- How to ingest data into a vector database, and use it.
- How LangChain4J's "Easy RAG" works, and a complete example using it.

Those demos either run locally (with Docker, using Ollama and Qdrant) or in the cloud (using Azure OpenAI or GitHub Models, and Azure AI Search).

## Slides

2 slide decks are available to detail this demo:

- [An introduction to LangChain4J](LangChain4J%20intro.pdf): a quick overview of LangChain4J
- [EasyRAG with LangChain4J](LangChain4J%20EasyRAG%20demo.pdf): a focus on demos 10 and 11, detailing the RAG pattern with LangChain4J

## Configuration

There are 4 Spring Boot profiles, so you can test the demos with different configurations, tools and models.

### _Option 1_ : Running in the cloud with Azure

This configuration uses:

- __Chat Model__: Azure OpenAI with gpt-4o
- __Image Model__: Azure OpenAI with dalle-3
- __Embedding model__: Azure OpenAI with text-embedding-ada
- __Embedding store__: Azure AI Search

It is enabled by using the `azure` Spring Boot profile.
One way to do this is to set `spring.profiles.active=azure` in the `src/main/resources/application.properties` file.

To provision the Azure resources, you need to run the `src/main/script/deploy-azure-openai-models.sh` script. It will create the following resources:

- An Azure OpenAI instance, with the necessary OpenAI models for this demo.
- An Azure AI Search instance.

At the end of this script, the following environment variables will be displayed (and stored in the `.env` file), and you will need them to run the application:
- `AZURE_OPENAI_ENDPOINT`: your Azure OpenAI URL endpoint.
- `AZURE_OPENAI_KEY`: your Azure OpenAI API key.
- `AZURE_SEARCH_ENDPOINT`: your Azure AI Search URL endpoint.
- `AZURE_SEARCH_KEY`: your Azure AI Search API key.

### _Option 2_ : Fully local, not very good, but small and fast

This configuration uses:

- __Chat Model__: Ollama with tinyllama
- __Image Model__: Not available
- __Embedding model__: in-memory Java with AllMiniLmL6V2EmbeddingModel
- __Embedding store__: Qdrant

It is enabled by using the `small` Spring Boot profile.
One way to do this is to set `spring.profiles.active=small` in the `src/main/resources/application.properties` file.

To set up the necessary resources, you need to have Docker installed on your machine, and run with Docker Compose the `src/main/docker/docker-compose-small.yml` file.

It will set up:

- An Ollama instance, with the tinyllama model.
- A Qdrant instance. Its Web UI is available at [http://localhost:6333/dashboard](http://localhost:6333/dashboard).

### _Option 3_ : Fully local, not very fast, but with good quality

This configuration uses:

- __Chat Model__: Ollama with Phi 3.5
- __Image Model__: Not available
- __Embedding model__: Ollama with nomic-embed-text
- __Embedding store__: Qdrant

It is enabled by using the `good` Spring Boot profile.
One way to do this is to set `spring.profiles.active=good` in the `src/main/resources/application.properties` file.

This configuration, especially when running inside Docker, requires a good amount of resources (CPU and RAM).
If you run into timeouts, that's because your machine is not powerful enough to run it.

__Improving performance__: if you have GPUs on your machine, Ollama performance can be greatly improved by using them. The easiest way is to install Ollama locally on your machine, and install the
models like in the `src/main/docker/install-ollama-models-good.sh` script.

To set up the necessary resources, you need to have Docker installed on your machine, and run with Docker Compose the `src/main/docker/docker-compose-good.yml` file.

It will set up:

- An Ollama instance, with the phi3.5 and the nomic-embed-text models. Its Web UI is available at [http://localhost:8081/](http://localhost:8081/).
- A Qdrant instance. Its Web UI is available at [http://localhost:6333/dashboard](http://localhost:6333/dashboard).

### _Option 4_ : GitHub Models

GitHub Models are available [here](https://github.com/marketplace/models).

This configuration uses:

- __Chat Model__: GitHub Models with gpt-4o-mini
- __Image Model__: Not available
- __Embedding model__: GitHub Models with text-embedding-3-small
- __Embedding store__: Qdrant

It is enabled by using the `github` Spring Boot profile.
One way to do this is to set `spring.profiles.active=github` in the `src/main/resources/application.properties` file.

To set up the necessary resources, you need to have Docker installed on your machine, and run with Docker Compose the `src/main/docker/docker-compose-github.yml` file.

It will set up:

- A Qdrant instance. Its Web UI is available at [http://localhost:6333/dashboard](http://localhost:6333/dashboard).

For accessing GitHub Models, you'll need an environment variable named `GITHUB_TOKEN` with a GitHub token that grants permission to access the models.

## Running the demos

Once the resources (Azure or local) are configured, you can run the demos using the following command:

```shell
./mvnw spring-boot:run
```

Then you can access the base URL, where you find the Web UI: [http://localhost:8080/](http://localhost:8080/).

The demos are available in the top menu.
