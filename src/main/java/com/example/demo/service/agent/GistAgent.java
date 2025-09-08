package com.example.demo.service.agent;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface GistAgent {

    @UserMessage("""
            Write the following content in a GitHub Gist, and return the URL of the created Gist:
            
            {{content}}
            """)
    @Agent("An agent that creates a GitHub Gist from the provided content.")
    String writeToGist(@V("content") String content);
}
