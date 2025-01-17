package com.example.demo.assistant.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.logging.Logger;

@Service
public class StockPriceService {

    private Logger log = Logger.getLogger(StockPriceService.class.getName());

    @Tool("Get the stock price of a company by its ticker")
    public double getStockPrice(@P("Company ticker") String ticker) {
        log.info("Getting stock price for " + ticker);
        if (Objects.equals(ticker, "MSFT")) {
            return 400.0;
        } else {
            return 0.0;
        }
    }
}
