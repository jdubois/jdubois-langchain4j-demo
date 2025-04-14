package com.example.demo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.assistant.json.Recipe;
import com.example.demo.assistant.json.TopAuthors;
import com.example.demo.assistant.mcp.McpAgent;
import com.example.demo.assistant.tool.ApplePieAgent;
import com.example.demo.assistant.tool.GistService;
import com.example.demo.assistant.tool.MarkdownService;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.image.ImageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.tool.ToolProvider;
import dev.langchain4j.store.embedding.EmbeddingStore;

@Controller
public class DemoController implements BeanFactoryAware {

    private final ImageModel imageModel;

    private final ChatLanguageModel chatLanguageModel;

    private final EmbeddingModel embeddingModel;

    private final EmbeddingStore<TextSegment> embeddingStore;

    private final GistService gistService;

    private final MarkdownService markdownService;

    private BeanFactory beanFactory;

    public DemoController(ImageModel imageModel, ChatLanguageModel chatLanguageModel, EmbeddingModel embeddingModel, EmbeddingStore<TextSegment> embeddingStore, GistService gistService, MarkdownService markdownService) {
        this.imageModel = imageModel;
        this.chatLanguageModel = chatLanguageModel;
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.gistService = gistService;
        this.markdownService = markdownService;
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
    String completeAgent(Model model) {
        String question = "I'm doing an apple pie, give me the list of ingredients that I need, transform it to Markdown and write it down in a GitHub gist";

        ApplePieAgent applePieAgent = AiServices.builder(ApplePieAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .tools(gistService, markdownService)
                .build();

        Recipe recipe = applePieAgent.getRecipe(question);

        return getView(model, "14: Agent with multiple tools and structured outputs", question, recipe.toString());
    }

    @GetMapping("/5")
    String mcpServer(Model model) {
        String question = "Who are the authors of the last 10 commits in the langchain4j/langchain4j repository, ordered by number of commits.";

        ToolProvider mcpToolProvider = beanFactory.getBean(ToolProvider.class);

        McpAgent mcpAgent = AiServices.builder(McpAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .toolProvider(mcpToolProvider)
                .build();

        TopAuthors topAuthors = mcpAgent.askGitHub(question);

        return getView(model, "15: Agent using an MCP Server", question, topAuthors.toString());
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
