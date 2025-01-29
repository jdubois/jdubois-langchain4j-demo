package com.example.demo;

import com.example.demo.assistant.tool.StockPriceService;
import com.example.demo.assistant.tool.StockAiAssistant;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DemoController {

    private final ChatLanguageModel chatLanguageModel;

    private final StockPriceService stockPriceService;

    public DemoController(ChatLanguageModel chatLanguageModel, StockPriceService stockPriceService) {
        this.chatLanguageModel = chatLanguageModel;
        this.stockPriceService = stockPriceService;
    }

    @GetMapping("/")
    public String demo() {
        return "demo";
    }

    @GetMapping("/stocks")
    String functionCalling(Model model) {
        String question = "Should I sell my Microsoft stocks today?";

        StockAiAssistant stockAiAssistant = AiServices.builder(StockAiAssistant.class)
                .chatLanguageModel(chatLanguageModel)
                .tools(stockPriceService)
                .build();

        String answer = stockAiAssistant.toolCallingChat(question);

        return getView(model, "Microsoft stock ", question, answer);
    }

    private static String getView(Model model, String demoName, String question, String answer) {
        model.addAttribute("demo", demoName);
        model.addAttribute("question", question);
        model.addAttribute("answer", answer);
        return "demo";
    }
}
