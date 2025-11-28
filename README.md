# LangChain4j demo

_Author: [Julien Dubois](https://www.julien-dubois.com)_

## Goal

This is a Spring Boot project that demonstrates how to use LangChain4j to create Java applications using LLMs.

It contains the following demos:

- How to generate an image using Dalle-3.
- How to generate text
  - In the cloud using GPT-5-mini on Microsoft Foundry
  - Locally with mistral running in Ollama
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

_This is the recommended option for the best experience. If environnement variables are not set, it will fallback to option 2 (running locally)_

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

This is the fallback option if the environment variables for option 1 are not set.

This configuration uses:

- __Chat Model__: Ollama with `mistral:7b` (see [https://ollama.com/library/mistral](https://ollama.com/library/mistral))
- __Image Model__: Not available
- __Embedding model__: Ollama with `nomic-embed-text` (see [https://ollama.com/library/nomic-embed-text](https://ollama.com/library/nomic-embed-text))
- __Embedding store__: Elasticsearch

To set up the necessary resources, you need to have Docker installed on your machine, and run with Docker Compose the `src/main/docker/docker-compose-small.yml` file.

It will set up:

- An Ollama instance, with the `mistral:7b` and `nomic-embed-text` models
- An Elasticsearch instance. Its Web UI, using [elasticvue](https://github.com/cars10/elasticvue), is available at [http://localhost:8081/](http://localhost:8081/)

__Demos using tools calling and MCP will not work correctly__, as this is not correctly supported with Ollama yet. If you still want to try it, you will need to provide a `GITHUB_TOKEN` environment variable as described in option 1.

__For faster inference__, you can also use Ollama natively on your machine. This will be noticeably faster if you have a GPU, as Ollama can leverage it.

- Install Ollama from [https://ollama.com/download](https://ollama.com/download)
- Download the models as in the `src/main/docker/install-ollama-models.sh` script
- Run Ollama locally instead of using the Docker container

### Advanced Configuration

We have seen in option 1 the following environment variables:

- `OPENAI_BASE_URL`: your Microsoft Foundry URL endpoint
- `OPENAI_API_KEY`: your Microsoft Foundry API key

They can be configured to use others, OpenAI-compatible services, for example GitHub Models or Docker Model Runners.

Here's an example using Docker Model Runners:

```shell
OPENAI_BASE_URL=http://localhost:12434/engines/llama.cpp/v1/
# The API key can be left empty as it is not required
OPENAI_API_KEY=
```

You can also modify the language models used by changing the following environment variables:

- `CHAT_MODEL_NAME`: the chat model to use (default: `gpt-5-mini` for Foundry, `mistral:7b` for Ollama)
- `IMAGE_MODEL_NAME`: the image model to use (default: `dalle-3` for Foundry, not available for Ollama)
- `EMBEDDING_MODEL_NAME`: the embedding model to use (default: `text-embedding-ada` for Foundry, `nomic-embed-text` for Ollama)

Here's an example with models available on Docker Model Runners:

```shell
CHAT_MODEL_NAME=ai/mistral:7B-Q4_K_M
EMBEDDING_MODEL_NAME=ai/nomic-embed-text-v1.5:137M-F16
# The API key can be left empty as it is not required
IMAGE_MODEL_NAME=
```

__Tip :__ you can store those environment variables in a `.env` file at the root of the project, and load them with the following command:

```shell
source .env
```

Or when using Docker, add the `--env-file .env` option to the `docker run` command:

```shell
docker run -p 8080:8080 --env-file .env langchain4j-demo
```

## Building and running the demos with the JVM

This project runs with Java, and uses Maven as the build tool.

### Prerequisites

Install Java 25 or later, for example:

```shell
# Using SDKMAN
sdk install java 25-tem
sdk use java 25-tem

# Using Homebrew (macOS)
brew install openjdk@25

```

### Quick Start

Environment variables need to be set up for running in the cloud (Microsoft Foundry and Azure AI Search), otherwise it will default to the local configuration (Ollama + Elasticsearch).

Once the resources are configured, you can run the demos using the following command:

```shell
export OPENAI_BASE_URL=...
export OPENAI_API_KEY=...
export AZURE_SEARCH_ENDPOINT=...
export AZURE_SEARCH_KEY=...
export GITHUB_TOKEN=...    # optional, for tools/MCP demos

./mvnw spring-boot:run
```

Or if you followed the tip above and stored the environment variables in a `.env` file:

```shell
source .env
./mvnw spring-boot:run
``` 

Then you can access the base URL, where you find the Web UI: [http://localhost:8080/](http://localhost:8080/).

That main page will also describe the current configuration: this will help you to know if you have configured the application correctly.

The demos are available in the menus at the top.

### Building and Running with the JVM-based Docker Image

If you prefer to containerize the Java binary, use the provided `Dockerfile` file.

To build the image, run the following commands:
```shell
# Build the container image
docker build -t langchain4j-demo .
```

To run the container, use the following command:`
```shell
# Run the container (pass env vars exactly like the JVM image)
docker run -p 8080:8080 \
  -e OPENAI_BASE_URL=... \
  -e OPENAI_API_KEY=... \
  -e AZURE_SEARCH_ENDPOINT=... \
  -e AZURE_SEARCH_KEY=... \
  langchain4j-demo
```

Or if you followed the tip above and stored the environment variables in a `.env` file:
```shell
docker run -p 8080:8080 --env-file .env langchain4j-demo
```

This multi-stage Dockerfile first compiles the Java executable, then copies only the resulting binary into a JVM-based image.

## Building and running the demos as a GraalVM Native Image

This project supports building a native image using GraalVM for instant startup and lower memory footprint.

**Note:** Tests are automatically skipped during native image builds to reduce compilation time.

### Prerequisites

Install GraalVM, for example:

```shell
# Using SDKMAN
sdk install java 25-graal
sdk use java 25-graal

# Using Homebrew (macOS)
brew install --cask graalvm-jdk
```

### Quick Start

```shell
./mvnw -Pnative native:compile

```

### Running the Native Image

```shell
# Export environment variables as needed
export OPENAI_BASE_URL=...
export OPENAI_API_KEY=...
export AZURE_SEARCH_ENDPOINT=...
export AZURE_SEARCH_KEY=...

# Run the native image
./target/demo
```

### Building and Running the Native Docker Image

If you prefer to containerize the GraalVM binary, use the provided `Dockerfile-native`.
This multi-stage Dockerfile first compiles the native executable with GraalVM, 
then copies only the resulting binary into a tiny distroless runtime image for fast startup and minimal footprint.

To build the image, run the following commands:
```shell
# Build the container image
docker build -f Dockerfile-native -t langchain4j-demo-native .
```

To run the container, use the following command:
```shell
# Run the container (pass env vars exactly like the JVM image)
docker run -p 8080:8080 \
  -e OPENAI_BASE_URL=... \
  -e OPENAI_API_KEY=... \
  -e AZURE_SEARCH_ENDPOINT=... \
  -e AZURE_SEARCH_KEY=... \
  langchain4j-demo-native
```

Or if you followed the tip above and stored the environment variables in a `.env` file:
```shell
docker run -p 8080:8080 --env-file .env langchain4j-demo-native
```
