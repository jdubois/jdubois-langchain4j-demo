package com.example.demo.service.mcp;

import com.example.demo.model.TopAuthors;

public interface GitHubMcpService {

    TopAuthors askGitHub(String userMessage);
}
