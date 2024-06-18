package com.example.demo;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.transformer.HtmlTextExtractor;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Controller
public class DemoController {

    private final ImageModel imageModel;

    private final ChatLanguageModel chatLanguageModel;

    private final EmbeddingModel embeddingModel;

    private final EmbeddingStore<TextSegment> embeddingStore;

    public DemoController(ImageModel imageModel, ChatLanguageModel chatLanguageModel, EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.imageModel = imageModel;
        this.chatLanguageModel = chatLanguageModel;
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    @GetMapping("/")
    public String demo() {
        return "demo";
    }

    @GetMapping("/1")
    String createImage(Model model) {
        String question = "A coffee mug in Paris, France";
        String answer = imageModel.generate(question).content().url().toString();

        model.addAttribute("demo", "1: image generation");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }

    @GetMapping("/2")
    String getAnswer(Model model) {
        String question = "Who painted the Mona Lisa?";
        String answer = chatLanguageModel.generate(UserMessage.from(question)).content().text();

        model.addAttribute("demo", "2: simple question");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }

    @GetMapping("/3")
    String reasonning(Model model) {
        String question = "Maria's father has 4 daugthers: Spring, Autumn, Winter. What is the name of the fourth daughter?";
        String answer = chatLanguageModel.generate(UserMessage.from(question)).content().text();

        model.addAttribute("demo", "3: Reasoning question");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }

    @GetMapping("/4")
    String getAnswerWithSystemMessage(Model model) {
        SystemMessage systemMessage = SystemMessage.from("I am the king of France. " +
                "Talk to me with extreme deference.");

        String question = "Who painted the Mona Lisa?";
        String answer = chatLanguageModel.generate(systemMessage, UserMessage.from(question)).content().text();

        model.addAttribute("demo", "4: advanced question");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }

    @GetMapping("/5")
    String getAnswerWithLocation(Model model) {
        String question = "Where can you see this painting?";
        String answer = chatLanguageModel.generate(UserMessage.from(question)).content().text();

        model.addAttribute("demo", "5: A question without memory");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }

    @GetMapping("/6")
    String getAnswerUsingConversationChain(Model model) {
        String context = "Who painted the Mona Lisa?";
        String question = "Where can you see this painting?";

        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(20);
        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(chatMemory)
                .build();

        chain.execute(context);
        String answer = chain.execute(question);

        model.addAttribute("demo", "6: A question with memory");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
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

        model.addAttribute("demo", "7: Data ingestion");
        model.addAttribute("question", "Ingesting data into the vector database");
        model.addAttribute("answer", "OK");
        return "demo";
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

        model.addAttribute("demo", "8: Querying the vector database");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }

    @GetMapping("/9")
    String queryVectorDatabaseWithDetails(Model model) {
        String question = "fruit";

        Embedding relevantEmbedding = embeddingModel.embed(question).content();
        EmbeddingSearchRequest relevantEmbeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(relevantEmbedding)
                .maxResults(3)
                .build();

        EmbeddingSearchResult<TextSegment> relevant = embeddingStore.search(relevantEmbeddingSearchRequest);

        String answer = relevant.matches().stream()
                .limit(3)
                .map(match -> match.embedded().text() + " | " + Arrays.toString(match.embedding().vector()))
                .collect(Collectors.joining("\n"));

        model.addAttribute("demo", "9: Getting the vectors from the vector database");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }

    @GetMapping("/10")
    String ingestNews(Model model) {
        Document document = UrlDocumentLoader.load("https://lite.cnn.com", new TextDocumentParser());

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentTransformer(new HtmlTextExtractor())
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestor.ingest(document);

        model.addAttribute("demo", "10: News ingestion");
        model.addAttribute("question", "Ingesting news into the vector database");
        model.addAttribute("answer", "OK");
        return "demo";
    }

    @GetMapping("/11")
    String rag(Model model) {
        String question = "The information that is given is a list of news, each of them starting with a \"*   \". Select one important event that occured in the US.";

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(new EmbeddingStoreContentRetriever(embeddingStore, embeddingModel, 20))
                .build();

        String answer = assistant.chat(question);

        model.addAttribute("demo", "11: Retrieval-Augmented Generation (RAG)");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }
}

interface Assistant {
    String chat(String userMessage);
}
