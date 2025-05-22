package com.example.demo;

import com.example.demo.assistant.json.Recipe;
import com.example.demo.assistant.mcp.McpAgent;
import com.example.demo.assistant.tool.ApplePieAgent;
import com.example.demo.assistant.tool.GistService;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    private final ChatModel chatModel;

    private final GistService gistService;

    private ToolProvider mcpToolProvider;

    public DemoController(ChatModel chatModel, GistService gistService, ToolProvider mcpToolProvider) {
        this.chatModel = chatModel;
        this.gistService = gistService;
        this.mcpToolProvider = mcpToolProvider;
    }

    @GetMapping("/")
    public String demo() {
        return "demo";
    }

    @GetMapping("/1")
    String getAnswer(Model model) {
        String question = "I'm doing an apple pie, give me the list of ingredients.";
        String answer = chatModel.chat(UserMessage.from(question)).aiMessage().text();
        return getView(model, "1: simple question", question, answer);
    }

    @GetMapping("/2")
    String structuredOutputs(Model model) {
        String question = "I'm doing an apple pie, give me the list of ingredients.";

        ApplePieAgent applePieAgent = AiServices.builder(ApplePieAgent.class)
                .chatModel(chatModel)
                .build();

        Recipe recipe = applePieAgent.getRecipe(question);

        return getView(model, "2: Structured Outputs", question, recipe.toString());
    }

    @GetMapping("/3")
    String functionCalling(Model model) {
        String question = """
        I'm doing an apple pie, give me the list of ingredients, 
        written in a GitHub gist.
        """;

        ApplePieAgent applePieAgent = AiServices.builder(ApplePieAgent.class)
                .chatModel(chatModel)
                .tools(gistService)
                .build();

        Recipe recipe = applePieAgent.getRecipe(question);

        return getView(model, "3: Function calling", question, recipe.toString());
    }

    @GetMapping("/4")
    String mcpServer(Model model) {
        String question = """
          I'm doing an apple pie, give me the list of ingredients in a MarkDown format, 
          and store the result in a file stored in an Azure Blob Storage.
          
          - As you can't create a local file, use an in-memory stream to pass the data to the Azure Blob Storage.
          - This Azure Blob Storage is called  called "judubois-applepie", create it if it doesn't exist yet.
          - It is stored in an Azure Blob Storage account named "juduboisingredients", create it if it doesn't exist yet.
          - Those resources are located in the Azure Resource Group tagged "env=demo"
          """;

        McpAgent mcpAgent = AiServices.builder(McpAgent.class)
                .chatModel(chatModel)
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
