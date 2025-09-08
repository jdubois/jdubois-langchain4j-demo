package com.example.demo.service.agent;

import com.example.demo.model.Author;
import com.example.demo.model.Item;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ListCreationTool {

    @Tool("""
            This tool uses the provided list of GitHub authors and recipe items to create a markdown formatted string.
            """)
    public String toMarkDown(@P("authors") List<Author> authors, @P("items") List<Item> items) {
        if (authors == null || authors.isEmpty() || items == null || items.isEmpty()) {
            return "No authors or items provided.";
        }

        Map<Author, List<Item>> authorItemMap = new HashMap<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        // Randomly assign each item to an author
        for (Item item : items) {
            Author randomAuthor = authors.get(random.nextInt(authors.size()));
            authorItemMap.computeIfAbsent(randomAuthor, k -> new java.util.ArrayList<>()).add(item);
        }

        // Build markdown string
        StringBuilder markdown = new StringBuilder();
        for (Map.Entry<Author, List<Item>> entry : authorItemMap.entrySet()) {
            Author author = entry.getKey();
            List<Item> authorItems = entry.getValue();

            markdown.append("# ").append(author.firstName()).append(" ").append(author.lastName()).append("\n\n");

            for (Item item : authorItems) {
                markdown.append("- ").append(item.title()).append(": ").append(item.description()).append("\n");
            }
            markdown.append("\n");
        }

        return markdown.toString();
    }
}
