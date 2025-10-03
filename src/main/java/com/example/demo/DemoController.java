package com.example.demo;

import com.example.demo.model.Recipe;
import com.example.demo.model.TopAuthors;
import com.example.demo.service.agent.GistAgent;
import com.example.demo.service.agent.GitHubAuthorsAgent;
import com.example.demo.service.agent.ListCreationTool;
import com.example.demo.service.agent.RecipeAgent;
import com.example.demo.service.agent.ShoppingListAgent;
import com.example.demo.service.mcp.GitHubMcpService;
import com.example.demo.service.tool.ApplePieService;
import com.example.demo.service.tool.GistService;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class DemoController implements BeanFactoryAware {

    private final ChatModel chatModel;

    private final GistService gistService;

    private BeanFactory beanFactory;

    public DemoController(ChatModel chatModel, GistService gistService) {
        this.chatModel = chatModel;
        this.gistService = gistService;
    }

    @GetMapping("/")
    public String demo() {
        return "demo";
    }

    @GetMapping("/1")
    String getAnswer(Model model) {
        String question = "I'm doing an apple pie, give me the list of ingredients.";
        String answer = chatModel.chat(UserMessage.from(question)).aiMessage().text();
        return getView(model, "1: Simple configuration", question, answer);
    }

    @GetMapping("/2")
    String structuredOutputs(Model model) {
        String question = "I'm doing an apple pie, give me the list of ingredients.";

        ApplePieService applePieService = AiServices.builder(ApplePieService.class)
                .chatModel(chatModel)
                .build();

        Recipe recipe = applePieService.getRecipe(question);

        return getView(model, "2: Structured Outputs", question, recipe.toString());
    }

    @GetMapping("/3")
    String functionCalling(Model model) {
        String question = "I'm doing an apple pie, give me the list of ingredients that I need, and write it down in a GitHub gist.";

        ApplePieService applePieService = AiServices.builder(ApplePieService.class)
                .chatModel(chatModel)
                .tools(gistService)
                .build();

        Recipe recipe = applePieService.getRecipe(question);

        return getView(model, "3: Function calling", question, recipe.toString());
    }

    @GetMapping("/4")
    String mcpServer(Model model) {
        String question = "Who are the authors of the last 20 commits in the langchain4j/langchain4j repository, ordered by number of commits.";

        ToolProvider gitHubMcpServer = beanFactory.getBean(ToolProvider.class);

        GitHubMcpService gitHubMcpService = AiServices.builder(GitHubMcpService.class)
                .chatModel(chatModel)
                .toolProvider(gitHubMcpServer)
                .build();

        TopAuthors topAuthors = gitHubMcpService.askGitHub(question);

        return getView(model, "4: Using an MCP Server", question, topAuthors.toString());
    }

    @GetMapping("/5")
    String simpleAgent(Model model) {

        // The agent to get the recipe is a standard agent
        RecipeAgent recipeAgent = AgenticServices
                .agentBuilder(RecipeAgent.class)
                .chatModel(chatModel)
                .build();

        Recipe recipe = recipeAgent.getRecipe("apple pie");

        return getView(model, "5: One agent", "One agent fetching data", recipe.toString());

    }

    @GetMapping("/6")
    String agentic(Model model) {

        // The agent to get the recipe is a standard agent
        RecipeAgent recipeAgent = AgenticServices
                .agentBuilder(RecipeAgent.class)
                .chatModel(chatModel)
                .outputName("recipe")
                .build();

        // The agent to get the GitHub authors uses the GitHub MCP server
        GitHubAuthorsAgent gitHubAuthorsAgent = AgenticServices
                .agentBuilder(GitHubAuthorsAgent.class)
                .chatModel(chatModel)
                .toolProvider(beanFactory.getBean(ToolProvider.class))
                .outputName("authors")
                .build();

        // The data agent runs the previous two agents in parallel
        UntypedAgent dataBrokerAgent = AgenticServices
                .parallelBuilder()
                .subAgents(recipeAgent, gitHubAuthorsAgent)
                .build();

        // The agent which creates the markdown list uses a tool to calculate the final list
        ShoppingListAgent shoppingListAgent = AgenticServices
                .agentBuilder(ShoppingListAgent.class)
                .chatModel(chatModel)
                .tools(new ListCreationTool())
                .outputName("content")
                .build();

        // The agent storing the final result in a GitHub gist uses a tool to call a Spring Bean
        GistAgent gistAgent = AgenticServices
                .agentBuilder(GistAgent.class)
                .chatModel(chatModel)
                .tools(gistService)
                .outputName("gistUrl")
                .build();

        // The supervisor agent coordinates the previous agents
        UntypedAgent supervisorAgent = AgenticServices
                .sequenceBuilder()
                .subAgents(dataBrokerAgent, shoppingListAgent, gistAgent)
                .outputName("gistUrl")
                .build();

        Map<String, Object> input = Map.of(
                "repository", "langchain4j/langchain4j",
                "recipeName", "apple pie"
        );

        String answer = (String) supervisorAgent.invoke(input);

        return getView(model, "6: Agents working together", "Agentic AI with 4 agents working together", answer);
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
