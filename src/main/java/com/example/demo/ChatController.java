package com.example.demo;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatLanguageModel model;

    public ChatController(ChatLanguageModel model) {
        this.model = model;
    }

    @GetMapping("/1")
    @ResponseBody
    String getAnswer() {
        return model.generate(UserMessage.from("Who painted the Mona Lisa?")).content().text();
    }

    @GetMapping("/2")
    @ResponseBody
    String getAnswerWithSystemMessage() {
        SystemMessage systemMessage = SystemMessage.from("I am the king of France. Talk to me with extreme deference.");
        return model.generate(systemMessage, UserMessage.from("Who painted the Mona Lisa?")).content().text();
    }

    @GetMapping("/3")
    @ResponseBody
    String getAnswerWithLocation() {
        return model.generate(UserMessage.from("Where can you see this painting?")).content().text();    }

    @GetMapping("/4")
    @ResponseBody
    String getAnswerUsingConversationChain() {
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(20);
        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(model)
                .chatMemory(chatMemory)
                .build();

        chain.execute("Who painted the Mona Lisa?");
        return chain.execute("Where can you see this painting?");
    }
}
