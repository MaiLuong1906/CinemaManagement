package ai;

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

    private static final String API_KEY = "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";
    private static final String MODEL = "llama-3.3-70b-versatile";
    private static final String API_URL = "https://api.groq.com/openai/v1/chat/completions";

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    private final ObjectMapper mapper = new ObjectMapper();

    public String generate(List<Message> messages) {
        try {
            ObjectNode body = mapper.createObjectNode();
            body.put("model", MODEL);

            ArrayNode msgs = mapper.createArrayNode();
            for (Message m : messages) {
                if (m.getContent() == null || m.getContent().isBlank()) continue;
                ObjectNode msg = mapper.createObjectNode();
                msg.put("role", m.getRole().equalsIgnoreCase("assistant")
                    ? "assistant" : m.getRole().toLowerCase());
                msg.put("content", m.getContent());
                msgs.add(msg);
            }

            if (msgs.isEmpty()) {
                return "Không có nội dung để xử lý.";
            }

            body.set("messages", msgs);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .timeout(Duration.ofSeconds(60))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            mapper.writeValueAsString(body)
                    ))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonNode root = mapper.readTree(response.body());

            if (root.has("error")) {
                throw new RuntimeException("API error: "
                    + root.get("error").get("message").asText());
            }

            return root.get("choices").get(0)
                       .get("message")
                       .get("content").asText();

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("API call failed: " + e.getMessage(), e);
        }
    }

    public String generate(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            return "Prompt không được để trống.";
        }
        return generate(List.of(new Message("user", prompt)));
    }
}