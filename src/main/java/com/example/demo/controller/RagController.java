package com.example.demo.controller;

import com.example.demo.service.rag.RagAssistant;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.document.transformer.jsoup.HtmlToTextDocumentTransformer;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * Controller for demonstrating Retrieval-Augmented Generation (RAG) using LangChain4j.
 * <p>
 * This controller showcases how to work with vector databases and implement RAG patterns:
 * <ul>
 *   <li>Simple data ingestion into a vector database</li>
 *   <li>Querying the vector database using embeddings</li>
 *   <li>Advanced data ingestion from web sources with HTML parsing and document splitting</li>
 *   <li>Complete RAG implementation using LangChain4j's "Easy RAG" feature</li>
 * </ul>
 * <p>
 * The demos can run either in the cloud or locally.
 * <p>
 * Configuration requirements:
 * <ul>
 *   <li>For cloud: Azure AI Search endpoint and API key</li>
 *   <li>For local: Elasticsearch running via Docker Compose (Web UI available at http://localhost:8081/)</li>
 * </ul>
 */
@Controller
public class RagController {

    private final ChatModel chatModel;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;

    public RagController(ChatModel chatModel, EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.chatModel = chatModel;
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    @GetMapping("/7")
    String loadVectorDatabase(Model model) {
        String content1 = "banana";
        String content2 = "computer";
        String content3 = "apple";
        String content4 = "pizza";
        String content5 = "strawberry";
        String content6 = "chess";

        List<String> contents = asList(content1, content2, content3, content4, content5, content6);

        for (String content : contents) {
            TextSegment textSegment = TextSegment.from(content);
            Embedding embedding = embeddingModel.embed(content).content();
            embeddingStore.add(embedding, textSegment);
        }
        return getView(model, "7: Simple data ingestion", "Ingesting data into the vector database", "OK");
    }

    @GetMapping("/8")
    String queryVectorDatabase(Model model) {
        String question = "fruit";

        Embedding relevantEmbedding = embeddingModel.embed(question).content();
        EmbeddingSearchRequest relevantEmbeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(relevantEmbedding)
                .maxResults(3)
                .build();

        EmbeddingSearchResult<TextSegment> relevant = embeddingStore.search(relevantEmbeddingSearchRequest);

        String answer = relevant.matches().stream()
                .limit(3)
                .map(match -> match.embedded().text())
                .collect(Collectors.joining("\n"));

        return getView(model, "8: Querying the vector database", question, answer);
    }

    @GetMapping("/9")
    String ingestNews(Model model) {
        var resource = getClass().getClassLoader().getResource("data/microsoft-2025-annual-report.html");
        if (resource == null) {
            throw new IllegalStateException("Resource not found: data/microsoft-2025-annual-report.html");
        }

        Document document = FileSystemDocumentLoader.loadDocument(resource.getPath(), new TextDocumentParser());

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentTransformer(new HtmlToTextDocumentTransformer(".annual-report"))
                .documentSplitter(DocumentSplitters.recursive(1000, 100))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestor.ingest(document);

        return getView(model, "9: Advanced data ingestion", "Ingesting news into the vector database", "OK");
    }

    @GetMapping("/10")
    String rag(Model model) {
        String question = "How many people are employed by Microsoft in the U.S. as of June 30, 2025?";

        RagAssistant ragAssistant = AiServices.builder(RagAssistant.class)
                .chatModel(chatModel)
                .contentRetriever(new EmbeddingStoreContentRetriever(embeddingStore, embeddingModel, 10))
                .build();

        String answer = ragAssistant.augmentedChat(question);

        return getView(model, "10: Retrieval-Augmented Generation (RAG)", question, answer);
    }

    private static String getView(Model model, String demoName, String question, String answer) {
        model.addAttribute("demo", demoName);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }
}

