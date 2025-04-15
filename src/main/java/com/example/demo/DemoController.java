package com.example.demo;

import com.example.demo.assistant.json.Recipe;
import com.example.demo.assistant.mcp.McpAgent;
import com.example.demo.assistant.tool.ApplePieAgent;
import com.example.demo.assistant.tool.GistService;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController implements BeanFactoryAware {

    private final ChatLanguageModel chatLanguageModel;

    private final GistService gistService;

    private BeanFactory beanFactory;

    public DemoController(ChatLanguageModel chatLanguageModel, GistService gistService) {
        this.chatLanguageModel = chatLanguageModel;
        this.gistService = gistService;
    }

    @GetMapping("/")
    public String demo() {
        return "demo";
    }

    @GetMapping("/1")
    String getAnswer(Model model) {
        String question = "Who painted the Mona Lisa?";
        String answer = chatLanguageModel.chat(UserMessage.from(question)).aiMessage().text();
        return getView(model, "2: simple question", question, answer);
    }

    @GetMapping("/2")
    String structuredOutputs(Model model) {
        String question = "I'm doing an apple pie, give me the list of ingredients.";

        ApplePieAgent applePieAgent = AiServices.builder(ApplePieAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .build();

        Recipe recipe = applePieAgent.getRecipe(question);

        return getView(model, "12: Structured Outputs", question, recipe.toString());
    }

    @GetMapping("/3")
    String functionCalling(Model model) {
        String question = "I'm doing an apple pie, give me the list of ingredients that I need, write it down in a GitHub gist.";

        ApplePieAgent applePieAgent = AiServices.builder(ApplePieAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .tools(gistService)
                .build();

        Recipe recipe = applePieAgent.getRecipe(question);

        return getView(model, "13: Function calling", question, recipe.toString());
    }

    @GetMapping("/4")
    String mcpServer(Model model) {
        String question = """
          I'm doing an apple pie, give me the list of ingredients that I need in a MarkDown format, and store the result in a file stored in an Azure Blob Storage.
          
          - As you can't create a local file, use an in-memory stream to pass the data to the Azure Blob Storage.
          - This Azure File Share is called  called "judubois-ingredients", create it if it doesn't exist yet.
          - It is stored in an Azure Blob Storage account named "juduboisapplepie", create it if it doesn't exist yet.
          - Those resources are located in the Azure Resource Group tagged 'env=demo'
          """;

        ToolProvider mcpToolProvider = beanFactory.getBean(ToolProvider.class);

        McpAgent mcpAgent = AiServices.builder(McpAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .toolProvider(mcpToolProvider)
                .build();

        String answer = mcpAgent.talkToAzure(question);

        return getView(model, "15: Agent using an MCP Server", question, answer);
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
