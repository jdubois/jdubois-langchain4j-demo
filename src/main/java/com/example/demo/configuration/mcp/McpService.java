package com.example.demo.configuration.mcp;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.List;

@Configuration
@Lazy
public class McpService {

    @Value("${AZURE_CREDENTIALS:}")
    private String azureCredentials;

    @Bean
    public ToolProvider mcpToolProvider() {
        McpTransport transport = new StdioMcpTransport.Builder()
                .command(List.of("docker", "run", "-e", "AZURE_CREDENTIALS=" + azureCredentials, "-i", "ghcr.io/jdubois/azure-cli-mcp:latest"))
                .logEvents(true)
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        return McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();
    }
}
