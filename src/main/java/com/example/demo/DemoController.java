package com.example.demo;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

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

        model.addAttribute("demo", "Demo 1: image generation");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }

    @GetMapping("/2")
    String getAnswer(Model model) {
        String question = "Who painted the Mona Lisa?";
        String answer = chatLanguageModel.generate(UserMessage.from(question)).content().text() + "\n";

        model.addAttribute("demo", "Demo 2: simple question");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }

    @GetMapping("/3")
    String getAnswerWithSystemMessage(Model model) {
        SystemMessage systemMessage = SystemMessage.from("I am the king of France. " +
                "Talk to me with extreme deference.");

        String question = "Who painted the Mona Lisa?";
        String answer = chatLanguageModel.generate(systemMessage, UserMessage.from(question)).content().text();

        model.addAttribute("demo", "Demo 3: advanced question");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }

    @GetMapping("/4")
    String getAnswerWithLocation(Model model) {
        String question = "Where can you see this painting?";
        String answer = chatLanguageModel.generate(UserMessage.from(question)).content().text();

        model.addAttribute("demo", "Demo 4: A question without memory");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }

    @GetMapping("/5")
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

        model.addAttribute("demo", "Demo 5: A question with memory");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }

    @GetMapping("/6")
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

        model.addAttribute("demo", "Demo 6: Data ingestion");
        model.addAttribute("question", "Ingesting data into the vector database");
        model.addAttribute("answer", "OK");
        return "demo";
    }

    @GetMapping("/7")
    String queryVectorDatabase(Model model) {
        String question = "fruit";

        Embedding relevantEmbedding = embeddingModel.embed(question).content();
        List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.findRelevant(relevantEmbedding, 3);

        String answer = relevant.get(0).embedded().text() + "\n";
        answer += relevant.get(1).embedded().text() + "\n";
        answer += relevant.get(2).embedded().text() + "\n";

        model.addAttribute("demo", "Demo 7: Querying the vector database");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }

    @GetMapping("/8")
    String queryVectorDatabaseWithDetails(Model model) {
        String question = "fruit";

        Embedding relevantEmbedding = embeddingModel.embed(question).content();
        List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.findRelevant(relevantEmbedding, 3);

        String answer = relevant.get(0).embedded().text() + " | " + Arrays.toString(relevant.get(0).embedding().vector()) + "\n";
        answer += relevant.get(1).embedded().text() + " | " + Arrays.toString(relevant.get(1).embedding().vector()) + "\n";
        answer += relevant.get(2).embedded().text() + " | " + Arrays.toString(relevant.get(2).embedding().vector()) + "\n";

        model.addAttribute("demo", "Demo 8: Getting the vectors from the vector database");
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }
}
