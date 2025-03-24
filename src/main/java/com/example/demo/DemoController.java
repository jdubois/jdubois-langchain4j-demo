package com.example.demo;

import com.example.demo.assistant.json.Recipe;
import com.example.demo.assistant.json.TopAuthors;
import com.example.demo.assistant.mcp.McpAgent;
import com.example.demo.assistant.rag.RagAssistant;
import com.example.demo.assistant.tool.ApplePieAgent;
import com.example.demo.assistant.tool.GistService;
import com.example.demo.assistant.tool.MarkdownService;
import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.document.transformer.jsoup.HtmlToTextDocumentTransformer;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialImageModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Controller
public class DemoController implements BeanFactoryAware {

    private final ImageModel imageModel;

    private final ChatLanguageModel chatLanguageModel;

    private final EmbeddingModel embeddingModel;

    private final EmbeddingStore<TextSegment> embeddingStore;

    private final GistService gistService;

    private final MarkdownService markdownService;

    private BeanFactory beanFactory;

    public DemoController(ImageModel imageModel, ChatLanguageModel chatLanguageModel, EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore, GistService gistService, MarkdownService markdownService) {
        this.imageModel = imageModel;
        this.chatLanguageModel = chatLanguageModel;
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.gistService = gistService;
        this.markdownService = markdownService;
    }

    @GetMapping("/")
    public String demo() {
        return "demo";
    }

    @GetMapping("/1")
    String createImage(Model model) {
        String question = "A coffee mug in Paris, France";
        if (imageModel instanceof OpenAiOfficialImageModel) {
            String answer = imageModel.generate(question).content().url().toString();
            return getView(model, "1: image generation", question, answer);
        } else {
            return getView(model, "1: image generation", question, "Image generation is only supported by Azure OpenAI");
        }
    }

    @GetMapping("/2")
    String getAnswer(Model model) {
        String question = "Who painted the Mona Lisa?";
        String answer = chatLanguageModel.chat(UserMessage.from(question)).aiMessage().text();
        return getView(model, "2: simple question", question, answer);
    }

    @GetMapping("/3")
    String reasoning(Model model) {
        String question = "Maria's father has 4 daughters: Spring, Autumn, Winter. What is the name of the fourth daughter?";
        String answer = chatLanguageModel.chat(UserMessage.from(question)).aiMessage().text();
        return getView(model, "3: Reasoning question", question, answer);
    }

    @GetMapping("/4")
    String getAnswerWithSystemMessage(Model model) {
        SystemMessage systemMessage = SystemMessage.from("I answer questions in French, in 100 words or less.");

        String question = "Give an explanation on how the Mona Lisa was painted.";
        String answer = chatLanguageModel.chat(systemMessage, UserMessage.from(question)).aiMessage().text();
        return getView(model, "4: advanced question", question, answer);
    }

    @GetMapping("/5")
    String getAnswerWithLocation(Model model) {
        String question = "Where can you see this painting?";
        String answer = chatLanguageModel.chat(UserMessage.from(question)).aiMessage().text();
        return getView(model, "5: A question without memory", question, answer);
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
        return getView(model, "6: A question with memory", question, answer);
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

        return getView(model, "9: Getting the vectors from the vector database", question, answer);
    }

    @GetMapping("/10")
    String ingestNews(Model model) {
        Document document = UrlDocumentLoader.load("https://www.microsoft.com/investor/reports/ar23/index.html", new TextDocumentParser());

        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentTransformer(new HtmlToTextDocumentTransformer(".annual-report"))
                .documentSplitter(DocumentSplitters.recursive(300, 30))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        ingestor.ingest(document);

        return getView(model, "10: Advanced data ingestion", "Ingesting news into the vector database", "OK");
    }

    @GetMapping("/11")
    String rag(Model model) {
        String question = "How many people are employed by Microsoft in the US?";

        RagAssistant ragAssistant = AiServices.builder(RagAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(new EmbeddingStoreContentRetriever(embeddingStore, embeddingModel, 3))
                .build();

        String answer = ragAssistant.augmentedChat(question);

        return getView(model, "11: Retrieval-Augmented Generation (RAG)", question, answer);
    }

    @GetMapping("/12")
    String structuredOutputs(Model model) {
        String question = "I'm doing an apple pie, give me the list of ingredients.";

        ApplePieAgent applePieAgent = AiServices.builder(ApplePieAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .build();

        Recipe recipe = applePieAgent.getRecipe(question);

        return getView(model, "12: Structured Outputs", question, recipe.toString());
    }

    @GetMapping("/13")
    String functionCalling(Model model) {
        String question = "I'm doing an apple pie, give me the list of ingredients that I need, write it down in a GitHub gist.";

        ApplePieAgent applePieAgent = AiServices.builder(ApplePieAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .tools(gistService)
                .build();

        Recipe recipe = applePieAgent.getRecipe(question);

        return getView(model, "13: Function calling", question, recipe.toString());
    }

    @GetMapping("/14")
    String completeAgent(Model model) {
        String question = "I'm doing an apple pie, give me the list of ingredients that I need, transform it to Markdown and write it down in a GitHub gist";

        ApplePieAgent applePieAgent = AiServices.builder(ApplePieAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .tools(gistService, markdownService)
                .build();

        Recipe recipe = applePieAgent.getRecipe(question);

        return getView(model, "14: Agent with multiple tools and structured outputs", question, recipe.toString());
    }

    @GetMapping("/15")
    String mcpServer(Model model) {
        String question = "Who are the authors of the last 10 commits in the langchain4j/langchain4j repository, ordered by number of commits.";

        ToolProvider mcpToolProvider = beanFactory.getBean(ToolProvider.class);

        McpAgent mcpAgent = AiServices.builder(McpAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .toolProvider(mcpToolProvider)
                .build();

        TopAuthors topAuthors = mcpAgent.findAzureTickets(question);

        return getView(model, "15: Agent using an MCP Server", question, topAuthors.toString());
    }

    private static String getView(Model model, String demoName, String question, String answer) {
        model.addAttribute("demo", demoName);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
