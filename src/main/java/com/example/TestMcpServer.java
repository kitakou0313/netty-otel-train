package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema.ServerCapabilities;

public class TestMcpServer {
    public static void main(String[] args) {
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider(new ObjectMapper());
        McpSyncServer server = McpServer.sync(transportProvider)
        .serverInfo("Test-Mcp", "1.0.0")
        .capabilities(ServerCapabilities.builder()
            .resources(true,true)
            .tools(true)
            .prompts(true)
            .logging()
            .build())
        .build();


        server.addTool(null);
    }
}
