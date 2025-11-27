package com.example.demo.service.tool;

import com.example.demo.model.Item;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MarkdownService {

    @Tool("Transform a list of items into Markdown")
    public String transformItemsToMarkdown(@P("List of items") List<Item> items) {
        String result = "";
        for (Item item : items) {
            if (item.completed()) {
                result += "- [x] " + item.title() + "\n";
            } else {
                result += "- [ ] " + item.title() + "\n";
            }
            if (Objects.nonNull(item.description())) {
                result += "  " + item.description() + "\n";
            }
        }
        return result;
    }
}
