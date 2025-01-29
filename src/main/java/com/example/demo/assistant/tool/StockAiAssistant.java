package com.example.demo.assistant.tool;

import dev.langchain4j.service.SystemMessage;

public interface StockAiAssistant {

    @SystemMessage("""
    If the closing price is 2% higher than the opening price, then it is a good moment to sell stocks.
    If the closing price is 3% lower than the opening price, then it is a good moment to buy stocks.
    """)
    String toolCallingChat(String userMessage);
}
