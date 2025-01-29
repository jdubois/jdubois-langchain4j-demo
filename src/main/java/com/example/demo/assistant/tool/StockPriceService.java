package com.example.demo.assistant.tool;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;

@Service
public class StockPriceService {

    private static final String API_URL = "https://api.tiingo.com/tiingo/daily/{ticker}/prices";

    @Value("${TIINGO_API_TOKEN}")
    private String API_TOKEN;

    private Logger log = Logger.getLogger(StockPriceService.class.getName());

    private final RestTemplate restTemplate;

    public StockPriceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Tool("Get the stock price of a company by its ticker")
    public Stock getStockPrice(@P("Company ticker") String ticker) {
        log.info("Getting stock price for " + ticker);
        String url = API_URL + "?token=" + API_TOKEN;
        Stock[] stocks = restTemplate.getForObject(url, Stock[].class, ticker);
        assert stocks != null;
        return stocks[0];
    }
}

record Stock(double open, double close) {}
