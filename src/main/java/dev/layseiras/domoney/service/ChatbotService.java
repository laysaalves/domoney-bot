package dev.layseiras.domoney.service;

import dev.layseiras.domoney.config.GeminiApi;
import dev.layseiras.domoney.dto.ChatbotRequest;
import dev.layseiras.domoney.service.generativeai.JsonService;
import dev.layseiras.domoney.service.generativeai.PromptBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ChatbotService {
    private final WebClient webClient;
    private final PromptBuilder promptBuilder;
    private final JsonService jsonService;

    public ChatbotService(GeminiApi gemini, PromptBuilder promptBuilder, JsonService jsonService) {
        this.webClient = WebClient.builder()
                .baseUrl(gemini.getApiUrl() + gemini.getApiKey())
                .defaultHeader("content-type", "application/json")
                .build();

        this.promptBuilder = promptBuilder;
        this.jsonService = jsonService;
    }

    public Mono<String> getChatbotOutput(ChatbotRequest request) {
        String prompt = promptBuilder.buildPrompt(request);
        String requestBody = jsonService.wrapPrompt(prompt);

        return webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .map(jsonService::extractChatbotResponse);
    }
}
