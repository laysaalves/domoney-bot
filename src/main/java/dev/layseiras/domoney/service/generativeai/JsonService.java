package dev.layseiras.domoney.service.generativeai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;

@Component
public class JsonService {
    private final ObjectMapper mapper;

    public JsonService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String wrapPrompt(String prompt) {
        try {
            ObjectNode part = mapper.createObjectNode();
            part.put("text", prompt);

            ArrayNode parts = mapper.createArrayNode().add(part);
            ObjectNode content = mapper.createObjectNode();
            content.set("parts", parts);

            ArrayNode contents = mapper.createArrayNode().add(content);
            ObjectNode root = mapper.createObjectNode();
            root.set("contents", contents);

            return mapper.writeValueAsString(root);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao montar JSON do input", e);
        }
    }

    public String extractChatbotResponse(String response) {
        try {
            JsonNode jsonNode = mapper.readTree(response);
            JsonNode candidates = jsonNode.path("candidates");

            if (candidates.isArray() && !candidates.isEmpty()) {
                JsonNode content = candidates.get(0).path("content");
                if (content.has("parts")) {
                    return content.get("parts").get(0).path("text").asText();
                }
            }
            return "Não foi possível gerar uma resposta do chat.";
        } catch (Exception e) {
            return "Erro ao processar a resposta da API: " + e.getMessage();
        }
    }
}

