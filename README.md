# LangChain4j demo

_Author: [Julien Dubois](https://www.julien-dubois.com)_

## Goal

This is a Spring Boot project that demonstrates how to use LangChain4j to create Java applications using LLMs.

It contains the following demos:

- How to generate an image using Dalle-3.
- How to generate text
  - In the cloud using GPT-5-mini on Microsoft Foundry
  - Locally with llama3.2 running in Ollama
- How to use a chat conversation with memory of the context
- How to ingest data into a vector database, and use it
  - In the cloud using Azure AI Search
  - Locally using Elasticsearch
- How LangChain4j's "Easy RAG" works, with a complete example using it
- How to use function calling
- How to use structured outputs (JSON schemas)
- How to use an MCP Server, using the GitHub MCP Server as an example
- How to use AI agents with LangChain4j's agentic module, with a complete example running multiple agents

Those demos either run locally (using Ollama and Elasticsearch) or in the cloud (using Microsoft Foundry and Azure AI Search).

## Configuration

Demos can either run in the cloud (using Microsoft Foundry and Azure AI Search) or locally (using Ollama and Elasticsearch).

### _Option 1_ : Running in the cloud with Microsoft Foundry and Azure AI Search

This configuration uses:

- __Chat Model__: Microsoft Foundry with gpt-5-mini
- __Image Model__: Microsoft Foundry with dalle-3
- __Embedding model__: Microsoft Foundry with text-embedding-ada
- __Embedding store__: Azure AI Search

To provision the Azure resources, you need to run the `src/main/script/deploy-microsoft-foundry-models.sh` script. It will create the following resources:

- A Microsoft Foundry instance, with the necessary OpenAI models for this demo
- An Azure AI Search instance

At the end of this script, the following environment variables will be displayed (and stored in the `.env` file), and you will need them to run the application:
- `OPENAI_BASE_URL`: your Microsoft Foundry URL endpoint
- `OPENAI_API_KEY`: your Microsoft Foundry API key
- `AZURE_SEARCH_ENDPOINT`: your Azure AI Search URL endpoint
- `AZURE_SEARCH_KEY`: your Azure AI Search API key

__For demos using tools calling and MCP__, we will need an additional environment variable to connect to GitHub:

- `GITHUB_TOKEN` is a GitHub personal access token
- It needs to be created following the instructions at [https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token).
- This token will need the following fine-grained permissions:
  - Read and Write access to Gists
  - _Optional_ Read access to Models: this will allow use to use GitHub Models as an alternative to Microsoft Foundry.

### _Option 2_ : Running locally with Ollama and Elasticsearch

This configuration uses:

- __Chat Model__: Ollama with `llama3.2:1b` (see [https://ollama.com/library/llama3.2](https://ollama.com/library/llama3.2))
- __Image Model__: Not available
- __Embedding model__: Ollama with `nomic-embed-text` (see [https://ollama.com/library/nomic-embed-text](https://ollama.com/library/nomic-embed-text))
- __Embedding store__: Elasticsearch

To set up the necessary resources, you need to have Docker installed on your machine, and run with Docker Compose the `src/main/docker/docker-compose-small.yml` file.

It will set up:

- An Ollama instance, with the `llama3.2:1b` and `nomic-embed-text` models
- An Elasticsearch instance. Its Web UI, using [elasticvue](https://github.com/cars10/elasticvue), is available at [http://localhost:8081/](http://localhost:8081/)

__Demos using tools calling and MCP will not work correctly__, as this is not correctly supported with Ollama yet.

__For faster inference__, you can also use Ollama natively on your machine. This will be noticeably faster if you have a GPU, as Ollama can leverage it.

- Install Ollama from [https://ollama.com/download](https://ollama.com/download)
- Download the models as in the `src/main/docker/install-ollama-models.sh` script
- Run Ollama locally instead of using the Docker container

## Running the demos

Environment variables need to be set up for running in the cloud (Microsoft Foundry and Azure AI Search), otherwise it will default to the local configuration (Ollama + Elasticsearch).

Once the resources are configured, you can run the demos using the following command:

```shell
./mvnw spring-boot:run
```

Then you can access the base URL, where you find the Web UI: [http://localhost:8080/](http://localhost:8080/).

That main page will also describe the current configuration: this will help you to know if you have configured the application correctly.

The demos are available in the menus at the top.
