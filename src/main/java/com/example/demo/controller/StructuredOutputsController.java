package com.example.demo.controller;

import com.example.demo.model.Recipe;
import com.example.demo.model.TopAuthors;
import com.example.demo.service.mcp.GitHubMcpService;
import com.example.demo.service.tool.ApplePieService;
import com.example.demo.service.tool.GistService;
import com.example.demo.service.tool.MarkdownService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for demonstrating structured outputs, function calling, and MCP integration with LangChain4j.
 * <p>
 * This controller showcases advanced LangChain4j features:
 * <ul>
 *   <li>Structured outputs using JSON schemas - Convert LLM responses to strongly-typed Java objects</li>
 *   <li>Function calling - Allow LLMs to call Java methods (tools) to perform actions</li>
 *   <li>Multiple tools coordination - Chaining multiple tool calls together</li>
 *   <li>Model Context Protocol (MCP) Server integration - Connect to external MCP servers like GitHub</li>
 * </ul>
 * <p>
 * The demos progressively build on each other:
 * <ol>
 *   <li>Basic structured output - Recipe object from LLM response</li>
 *   <li>Structured output with single tool - Recipe + GitHub Gist creation</li>
 *   <li>Structured output with multiple tools - Recipe + Markdown conversion + GitHub Gist</li>
 *   <li>MCP Server integration - Query GitHub repository data via MCP</li>
 * </ol>
 * <p>
 * Configuration requirements:
 * <ul>
 *   <li>A {@link ChatModel} (either cloud-based or local)</li>
 *   <li>GitHub personal access token with Gist read/write permissions (for demos 12-14)</li>
 *   <li>GitHub MCP Server configured via {@link ToolProvider} (for demo 14)</li>
 * </ul>
 * <p>
 * The GitHub personal access token needs the following fine-grained permissions:
 * <ul>
 *   <li>Read and Write access to Gists</li>
 * </ul>
 */
@Controller
public class StructuredOutputsController implements BeanFactoryAware {

    private final ChatModel chatModel;
    private final GistService gistService;
    private final MarkdownService markdownService;
    private BeanFactory beanFactory;

    public StructuredOutputsController(ChatModel chatModel, GistService gistService, MarkdownService markdownService) {
        this.chatModel = chatModel;
        this.gistService = gistService;
        this.markdownService = markdownService;
    }

    @GetMapping("/11")
    String structuredOutputs(Model model) {
        String question = "I'm doing an apple pie, give me the list of ingredients.";

        ApplePieService applePieAgent = AiServices.builder(ApplePieService.class)
                .chatModel(chatModel)
                .build();

        Recipe recipe = applePieAgent.getRecipe(question);

        return getView(model, "11: Structured Outputs", question, recipe.toString());
    }

    @GetMapping("/12")
    String functionCalling(Model model) {
        String question = "I'm doing an apple pie, give me the list of ingredients that I need, write it down in a GitHub gist.";

        ApplePieService applePieService = AiServices.builder(ApplePieService.class)
                .chatModel(chatModel)
                .tools(gistService)
                .build();

        Recipe recipe = applePieService.getRecipe(question);

        return getView(model, "12: Function calling", question, recipe.toString());
    }

    @GetMapping("/13")
    String multipleToolsAndStructuredOutput(Model model) {
        String question = "I'm doing an apple pie, give me the list of ingredients that I need, transform it to Markdown and write it down in a GitHub gist";

        ApplePieService applePieService = AiServices.builder(ApplePieService.class)
                .chatModel(chatModel)
                .tools(gistService, markdownService)
                .build();

        Recipe recipe = applePieService.getRecipe(question);

        return getView(model, "13: Multiple tools and structured outputs", question, recipe.toString());
    }

    @GetMapping("/14")
    String mcpServer(Model model) {
        String question = "Who are the authors of the last 10 commits in the langchain4j/langchain4j repository, ordered by number of commits.";

        ToolProvider gitHubMcpServer = beanFactory.getBean(ToolProvider.class);

        GitHubMcpService gitHubMcpService = AiServices.builder(GitHubMcpService.class)
                .chatModel(chatModel)
                .toolProvider(gitHubMcpServer)
                .build();

        TopAuthors topAuthors = gitHubMcpService.askGitHub(question);

        return getView(model, "14: Using an MCP Server", question, topAuthors.toString());
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

