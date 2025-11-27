package com.example.demo.controller;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.model.openaiofficial.OpenAiOfficialImageModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for basic LangChain4j demonstrations.
 * <p>
 * This controller provides fundamental examples of using LangChain4j, including:
 * <ul>
 *   <li>Image generation using DALL-E 3</li>
 *   <li>Simple text generation using GPT-5-mini or mistral</li>
 *   <li>Reasoning capabilities demonstration</li>
 *   <li>System message configuration for controlling AI behavior</li>
 *   <li>Conversation chains with and without memory</li>
 * </ul>
 * <p>
 * The demos can run either in the cloud (using Microsoft Foundry) or locally (using Ollama).
 * <ul>
 *   <li><b>Cloud</b>: Microsoft Foundry with gpt-5-mini and dalle-3</li>
 *   <li><b>Local</b>: Ollama with mistral:7b (note: image generation not available locally)</li>
 * </ul>
 * <p>
 * Configuration requirements:
 * <ul>
 *   <li>For cloud: Microsoft Foundry endpoint and API key</li>
 *   <li>For local: Ollama running with mistral:7b model</li>
 * </ul>
 */
@Controller
public class BasicDemosController {

    private final ImageModel imageModel;
    private final ChatModel chatModel;

    public BasicDemosController(ImageModel imageModel, ChatModel chatModel) {
        this.imageModel = imageModel;
        this.chatModel = chatModel;
    }

    @GetMapping("/1")
    String createImage(Model model) {
        String question = "A coffee mug in Paris, France";
        if (imageModel instanceof OpenAiOfficialImageModel) {
            String answer = imageModel.generate(question).content().url().toString();
            return getView(model, "1: image generation", question, answer);
        } else {
            return getView(model, "1: image generation", question, "Image generation is only supported by Microsoft Foundry");
        }
    }

    @GetMapping("/2")
    String getAnswer(Model model) {
        String question = "Who painted the Mona Lisa?";
        String answer = chatModel.chat(UserMessage.from(question)).aiMessage().text();
        return getView(model, "2: simple question", question, answer);
    }

    @GetMapping("/3")
    String reasoning(Model model) {
        String question = "Maria's father has 4 daughters: Spring, Autumn, Winter. What is the name of the fourth daughter?";
        String answer = chatModel.chat(UserMessage.from(question)).aiMessage().text();
        return getView(model, "3: Reasoning question", question, answer);
    }

    @GetMapping("/4")
    String getAnswerWithSystemMessage(Model model) {
        SystemMessage systemMessage = SystemMessage.from("I answer questions in French, in 100 words or less.");

        String question = "Give an explanation on how the Mona Lisa was painted.";
        String answer = chatModel.chat(systemMessage, UserMessage.from(question)).aiMessage().text();
        return getView(model, "4: advanced question", question, answer);
    }

    @GetMapping("/5")
    String getAnswerWithLocation(Model model) {
        String question = "Where can you see this painting?";
        String answer = chatModel.chat(UserMessage.from(question)).aiMessage().text();
        return getView(model, "5: A question without memory", question, answer);
    }

    @GetMapping("/6")
    String getAnswerUsingConversationChain(Model model) {
        String context = "Who painted the Mona Lisa?";
        String question = "Where can you see this painting?";

        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(20);
        ConversationalChain chain = ConversationalChain.builder()
                .chatModel(chatModel)
                .chatMemory(chatMemory)
                .build();

        chain.execute(context);
        String answer = chain.execute(question);
        return getView(model, "6: A question with memory", question, answer);
    }

    private static String getView(Model model, String demoName, String question, String answer) {
        model.addAttribute("demo", demoName);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }
}

