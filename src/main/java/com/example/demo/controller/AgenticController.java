package com.example.demo.controller;

import com.example.demo.service.agent.GistAgent;
import com.example.demo.service.agent.GitHubAuthorsAgent;
import com.example.demo.service.agent.ListCreationTool;
import com.example.demo.service.agent.RecipeAgent;
import com.example.demo.service.agent.ShoppingListAgent;
import com.example.demo.service.tool.GistService;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * Controller for demonstrating AI agents working together using LangChain4j's agentic module.
 * <p>
 * This controller showcases how to use AI agents with LangChain4j's agentic module, with a complete
 * example running multiple agents that coordinate to accomplish complex tasks. The demo includes:
 * <ul>
 *   <li>Recipe agent - Retrieves recipe information</li>
 *   <li>GitHub authors agent - Queries GitHub repository data using MCP Server</li>
 *   <li>Data broker agent - Runs multiple agents in parallel</li>
 *   <li>Shopping list agent - Creates formatted lists using tools</li>
 *   <li>Gist agent - Stores results in GitHub gists</li>
 *   <li>Supervisor agent - Coordinates all agents in sequence</li>
 * </ul>
 * <p>
 * The agents work together to fetch recipe ingredients, query GitHub repository information,
 * create a shopping list, and store the final result in a GitHub gist.
 * <p>
 * Configuration requirements:
 * <ul>
 *   <li>A {@link ChatModel} (either cloud-based or local)</li>
 *   <li>GitHub personal access token with Gist read/write permissions</li>
 *   <li>GitHub MCP Server configured via {@link ToolProvider}</li>
 * </ul>
 */
@Controller
public class AgenticController implements BeanFactoryAware {

    private final ChatModel chatModel;
    private final GistService gistService;
    private BeanFactory beanFactory;

    public AgenticController(ChatModel chatModel, GistService gistService) {
        this.chatModel = chatModel;
        this.gistService = gistService;
    }

    @GetMapping("/15")
    String agentic(Model model) {

        // The agent to get the recipe is a standard agent
        RecipeAgent recipeAgent = AgenticServices
                .agentBuilder(RecipeAgent.class)
                .chatModel(chatModel)
                .outputKey("recipe")
                .build();

        // The agent to get the GitHub authors uses the GitHub MCP server
        GitHubAuthorsAgent gitHubAuthorsAgent = AgenticServices
                .agentBuilder(GitHubAuthorsAgent.class)
                .chatModel(chatModel)
                .toolProvider(beanFactory.getBean(ToolProvider.class))
                .outputKey("authors")
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
                .outputKey("content")
                .build();

        // The agent storing the final result in a GitHub gist uses a tool to call a Spring Bean
        GistAgent gistAgent = AgenticServices
                .agentBuilder(GistAgent.class)
                .chatModel(chatModel)
                .tools(gistService)
                .outputKey("gistUrl")
                .build();

        // The supervisor agent coordinates the previous agents
        UntypedAgent supervisorAgent = AgenticServices
                .sequenceBuilder()
                .subAgents(dataBrokerAgent, shoppingListAgent, gistAgent)
                .outputKey("gistUrl")
                .build();

        Map<String, Object> input = Map.of(
                "repository", "langchain4j/langchain4j",
                "recipeName", "apple pie"
        );

        String answer = (String) supervisorAgent.invoke(input);

        return getView(model, "15: Agents working together", "Agentic AI with 4 agents working together", answer);
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

