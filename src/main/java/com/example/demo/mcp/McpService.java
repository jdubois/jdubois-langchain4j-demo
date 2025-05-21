package com.example.demo.mcp;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.http.HttpMcpTransport;
import dev.langchain4j.service.tool.ToolProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class McpService {

    @Bean
    public ToolProvider mcpToolProvider() {
        McpTransport transport = new HttpMcpTransport.Builder()
                .sseUrl("https://ca-pizza-mcp-vqqlxwmln5lf4.proudglacier-687aa477.eastus2.azurecontainerapps.io/sse")
                .build();

        McpClient mcpClient = new DefaultMcpClient.Builder()
                .transport(transport)
                .build();

        return McpToolProvider.builder()
                .mcpClients(List.of(mcpClient))
                .build();
    }
}
