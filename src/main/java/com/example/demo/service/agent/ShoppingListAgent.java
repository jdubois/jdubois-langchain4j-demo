package com.example.demo.service.agent;

import com.example.demo.model.Recipe;
import com.example.demo.model.TopAuthors;
import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface ShoppingListAgent {

    @UserMessage("""
            You are creating a shopping list for the recipe, formatted in MarkDown. For each item in the recipe, you need to assign an author from the list of authors.
            
            For this, you need:
            
            - The list of authors that will do the recipe: {{authors}}
            - The recipe with the list of items: {{recipe}}
            """)
    @Agent("An agent that creates a shopping list in MarkDown format based on the provided authors and recipe.")
    String createList(@V("authors") TopAuthors authors, @V("recipe") Recipe recipe);
}
