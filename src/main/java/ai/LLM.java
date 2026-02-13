/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai;

/**
 *
 * @author nguye
 */

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class LLM {

    private static final String OLLAMA_URL = "http://localhost:11434/api/chat";

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Dùng cho agent (nhiều message)
     */
    public String generate(List<Message> messages) {
        try {
            ObjectNode body = mapper.createObjectNode();
            body.put("model", "gemma3:4b");
            body.put("stream", false);
            ArrayNode msgArray = mapper.createArrayNode();
            for (Message m : messages) {
                ObjectNode msg = mapper.createObjectNode();
                msg.put("role", normalizeRole(m.getRole()));
                msg.put("content", m.getContent());
                msgArray.add(msg);
            }
            body.set("messages", msgArray);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OLLAMA_URL))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(60))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            mapper.writeValueAsString(body)
                    ))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());
            return root.get("message").get("content").asText();

        } catch (Exception e) {
            throw new RuntimeException("Ollama call failed", e);
        }
    }
    public String generate(String prompt) {
        return generate(List.of(
                new Message("user", prompt)
        ));
    }
    private String normalizeRole(String role) {
        if (role == null) return "user";
        return switch (role.toLowerCase()) {
            case "system", "user", "assistant" -> role.toLowerCase();
            default -> "user";
        };
    }
}

