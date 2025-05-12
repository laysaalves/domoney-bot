package dev.layseiras.domoney.controller;

import dev.layseiras.domoney.dto.ChatbotRequest;
import dev.layseiras.domoney.service.ChatbotService;
import dev.layseiras.domoney.service.chunks.ChunkReadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;
    private final ChunkReadService chunkReadService;

    public ChatbotController(ChatbotService chatbotService, ChunkReadService chunkReadService) {
        this.chatbotService = chatbotService;
        this.chunkReadService = chunkReadService;
    }

    @PostMapping("/ask")
    public Mono<ResponseEntity<String>> ask(@RequestBody ChatbotRequest request) {

        List<String> relevantChunks = chunkReadService.getRelevantChunks(request.userInput());


        if (relevantChunks.isEmpty()) {
            return Mono.just(ResponseEntity.ok("Não foi encontrado nenhum registro correspondente à sua pergunta."));
        } else {
            System.out.println(relevantChunks);
            return chatbotService.getChatbotOutput(request)
                    .map(ResponseEntity::ok)
                    .onErrorResume(e -> Mono.just(ResponseEntity.status(500).body("Erro: " + e.getMessage())));
        }
    }
}