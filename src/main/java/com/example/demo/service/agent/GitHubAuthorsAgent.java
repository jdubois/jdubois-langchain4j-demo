package com.example.demo.service.agent;

import com.example.demo.model.TopAuthors;
import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface GitHubAuthorsAgent {

    @UserMessage("""
            Get a list of the authors of the last 10 commits in the "{{repository}}" repository, ordered by number of commits.
            """)
    @Agent("An agent that retrieves the last GitHub authors from the provided GitHub repository.")
    TopAuthors getLastGitHubAuthors(@V("repository") String repository);
}
