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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

@RestController
public class ChatController {

    private final ImageModel imageModel;

    private final ChatLanguageModel chatLanguageModel;

    private final EmbeddingModel embeddingModel;

    private final EmbeddingStore<TextSegment> embeddingStore;

    public ChatController(ImageModel imageModel, ChatLanguageModel chatLanguageModel, EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore) {
        this.imageModel = imageModel;
        this.chatLanguageModel = chatLanguageModel;
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
    }

    @GetMapping("/0")
    @ResponseBody
    String createImage() {
        return imageModel.generate("A coffee mug in Paris, France").content().url().toString() + "\n";
    }

    @GetMapping("/1")
    @ResponseBody
    String getAnswer() {
        return chatLanguageModel.generate(UserMessage.from("Who painted the Mona Lisa?")).content().text() + "\n";
    }

    @GetMapping("/2")
    @ResponseBody
    String getAnswerWithSystemMessage() {
        SystemMessage systemMessage = SystemMessage.from("I am the king of France. Talk to me with extreme deference.");
        return chatLanguageModel.generate(systemMessage, UserMessage.from("Who painted the Mona Lisa?")).content().text() + "\n";
    }

    @GetMapping("/3")
    @ResponseBody
    String getAnswerWithLocation() {
        return chatLanguageModel.generate(UserMessage.from("Where can you see this painting?")).content().text() + "\n";
    }

    @GetMapping("/4")
    @ResponseBody
    String getAnswerUsingConversationChain() {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(20);
        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(chatMemory)
                .build();

        chain.execute("Who painted the Mona Lisa?");
        return chain.execute("Where can you see this painting?") + "\n";
    }

    @GetMapping("/5")
    @ResponseBody
    String loadVectorDatabase() {
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
        return "ok\n";
    }

    @GetMapping("/6")
    @ResponseBody
    String queryVectorDatabase() {
        Embedding relevantEmbedding = embeddingModel.embed("fruit").content();
        List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.findRelevant(relevantEmbedding, 3);

        String response = relevant.get(0).embedded().text() + "\n";
        response += relevant.get(1).embedded().text() + "\n";
        response += relevant.get(2).embedded().text() + "\n";
        return response;
    }

    @GetMapping("/7")
    @ResponseBody
    String queryVectorDatabaseWithDetails() {
        Embedding relevantEmbedding = embeddingModel.embed("fruit").content();
        List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.findRelevant(relevantEmbedding, 3);

        String response = relevant.get(0).embedded().text() + " | " + Arrays.toString(relevant.get(0).embedding().vector()) + "\n";
        response += relevant.get(1).embedded().text() + " | " + Arrays.toString(relevant.get(1).embedding().vector()) + "\n";
        response += relevant.get(2).embedded().text() + " | " + Arrays.toString(relevant.get(2).embedding().vector()) + "\n";
        return response;
    }
}
