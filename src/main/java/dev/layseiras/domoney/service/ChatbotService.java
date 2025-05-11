package dev.layseiras.domoney.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.layseiras.domoney.config.GeminiApi;
import dev.layseiras.domoney.dto.ChatbotRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatbotService {
    private final ChunkReadService chunkReadService;
    private final WebClient webClient;
    private final String apiUrl;
    private final String apiKey;

    @Autowired
    public ChatbotService(GeminiApi gemini, ChunkReadService chunkReadService) {
        this.apiKey = gemini.getApiKey();
        this.apiUrl = gemini.getApiUrl() + apiKey;
        this.chunkReadService = chunkReadService;

        this.webClient = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader("content-type", "application/json")
                .build();
    }

    public String processUserInput(ChatbotRequest request) {
        String systemPrompt = buildSystemPrompt(request.userInput());
        String contextHistory = buildContextHistory(request.context());
        String userPrompt = "Cliente: " + request.userInput();

        String fullPrompt = String.join("\n", systemPrompt, contextHistory, userPrompt);

        return wrapInJson(fullPrompt);
    }

    private static final String SYSTEM_INSTRUCTIONS = """
    Você é um assistente virtual de um sistema de finanças pessoais.
    
    Sua função é responder perguntas do usuário com base nas informações financeiras extraídas do seu dashboard, como tipos de pagamento, valores, status e métodos utilizados.
    
    Utilize apenas os dados fornecidos abaixo para construir suas respostas. Seja objetivo, claro e evite suposições. Se os dados não contiverem informações suficientes para responder à pergunta, informe claramente que não há registros correspondentes.
    
    Dados financeiros:
    %s
    """;

    private String buildSystemPrompt(String userInput) {
        List<String> relevantChunks = chunkReadService.getRelevantChunks(userInput);
        String context = String.join("\n", relevantChunks);
        return SYSTEM_INSTRUCTIONS.formatted(context);
    }

    private String buildContextHistory(List<String> contextMessages) {
        if (contextMessages == null || contextMessages.isEmpty()) return "";

        return contextMessages.stream()
                .map(msg -> "Histórico:\n" + msg)
                .collect(Collectors.joining("\n"));
    }

    private String wrapInJson(String prompt) {
        try {
            ObjectMapper mapper = new ObjectMapper();

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


    public Mono<String> getChatbotOutput(ChatbotRequest request) {
        String requestBody = processUserInput(request);

        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    try {
                        ObjectMapper mapper = new ObjectMapper();
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
                        e.printStackTrace();
                        return "Erro ao processar a resposta da API: " + e.getMessage();
                    }
                });
    }
}
