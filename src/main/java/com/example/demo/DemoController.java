package com.example.demo;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    private final ChatLanguageModel chatLanguageModel;

    private ToolProvider mcpToolProvider;

    public DemoController(ChatLanguageModel chatLanguageModel, ToolProvider mcpToolProvider) {
        this.chatLanguageModel = chatLanguageModel;
        this.mcpToolProvider = mcpToolProvider;
    }

    @GetMapping("/")
    public String demo() {
        return "demo";
    }

    @GetMapping("/1")
    String getAnswer(Model model) {
        String question = """
            I'm doing an apple pie, give me the list of ingredients, 
            formatted as an HTML list.
            """;
        String answer = chatLanguageModel
            .chat(UserMessage.from(question))
            .aiMessage()
            .text();
        return getView(model, "1: simple question", question, answer);
    }

    @GetMapping("/2")
    String mcpServer(Model model) {
        String question = """
          I'm doing an apple pie, give me the list of ingredients in a MarkDown format, 
          and store the result in a file stored in an Azure Blob Storage.
          
          - You should ONLY work in a resource group tagged "owner=judubois". If you can't find it, STOP doing anything and tell the user.
          - As you can't create a local file, use an in-memory stream to pass the data to the Azure Blob Storage.
          - This Azure Blob Storage is called "judubois-applepie", create it if it doesn't exist yet.
          - It is stored in an Azure Blob Storage account named "juduboisingredients", create it if it doesn't exist yet.
          """;

        McpAgent mcpAgent = AiServices.builder(McpAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .toolProvider(mcpToolProvider)
                .build();

        String answer = mcpAgent.talkToAzure(question);

        return getView(model, "4: Agent using an MCP Server", question, answer);
    }

    private static String getView(Model model, String demoName, String question, String answer) {
        model.addAttribute("demo", demoName);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }
}
