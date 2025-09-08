package com.example.demo.service.agent;

import com.example.demo.model.Recipe;
import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface RecipeAgent {

    @UserMessage("""
            I'm doing a recipe called "{{recipeName}}", give me the list of ingredients that I need.
            """)
    @Agent("An agent that provides a recipe based on the recipe name.")
    Recipe getRecipe(@V("recipeName") String recipeName);

}

