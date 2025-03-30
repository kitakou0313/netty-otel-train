package com.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.modelcontextprotocol.server.*;
import io.modelcontextprotocol.spec.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TempoTraceMcpServer {
    private static final String TEMPO_API_BASE_URL = "http://localhost:3200/tempo/api/traces";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final McpSyncServer mcpServer;

    public TempoTraceMcpServer() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        
        StdioServerTransportProvider transportProvider = new StdioServerTransportProvider(objectMapper);
        this.mcpServer = McpServer.sync(transportProvider)
            .serverInfo("Tempo-Trace-Retrieval", "1.0.0")
            .capabilities(ServerCapabilities.builder()
                .resources(true, true)
                .tools(true)
                .prompts(true)
                .logging()
                .build())
            .build();
    }

    private void setupTraceSearchTool() {
        McpServerFeatures.SyncToolSpecification searchTool = new McpServerFeatures.SyncToolSpecification(
            new Tool(TEMPO_API_BASE_URL, TEMPO_API_BASE_URL, null), 
        (request, arh) -> {
            try {
                // Extract search parameters
                String serviceName = request.getParams().optString("service_name", null);
                String startTime = request.getParams().optString("start_time", null);
                String endTime = request.getParams().optString("end_time", null);
                int limit = request.getParams().optInt("limit", 10);

                // Construct Tempo API request URL
                URI uri = URI.create(TEMPO_API_BASE_URL + "?limit=" + limit +
                    (serviceName != null ? "&serviceName=" + serviceName : "") +
                    (startTime != null ? "&start=" + startTime : "") +
                    (endTime != null ? "&end=" + endTime : ""));

                // Execute HTTP request
                HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

                HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                
                // Parse and return traces
                JsonNode tracesNode = objectMapper.readTree(response.body());
                return McpServerFeatures.toolResponse(tracesNode.toString());
            } catch (Exception e) {
                return McpServerFeatures.toolResponse("Error searching traces: " + e.getMessage(), true);
            }
        });
        
        mcpServer.addTool("search_traces", searchTool);
    }

    private void setupTraceDetailsTool() {
        McpServerFeatures.SyncToolRegistration detailsTool = (request) -> {
            try {
                String traceId = request.getParams().getString("trace_id");

                // Construct Tempo API request URL
                URI uri = URI.create(TEMPO_API_BASE_URL + "/" + traceId);

                // Execute HTTP request
                HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();

                HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                
                // Parse and return trace details
                JsonNode traceNode = objectMapper.readTree(response.body());
                return McpServerFeatures.toolResponse(traceNode.toString());
            } catch (Exception e) {
                return McpServerFeatures.toolResponse("Error retrieving trace details: " + e.getMessage(), true);
            }
        };
        
        mcpServer.addTool("get_trace_details", detailsTool);
    }

    public void run() throws Exception {
        setupTraceSearchTool();
        setupTraceDetailsTool();
    }

    public static void main(String[] args) throws Exception {
        new TempoTraceMcpServer().run();
    }
}
