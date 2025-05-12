package dev.layseiras.domoney.service.generativeai;

import dev.layseiras.domoney.dto.ChatbotRequest;
import dev.layseiras.domoney.service.chunks.ChunkReadService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PromptBuilder {
    private final ChunkReadService chunkReadService;

    public PromptBuilder(ChunkReadService chunkReadService) {
        this.chunkReadService = chunkReadService;
    }

    private static final String SYSTEM_INSTRUCTIONS = """
    Você é um assistente virtual de um sistema de finanças pessoais.
    
    Sua função é responder perguntas do usuário com base nas informações financeiras extraídas do seu dashboard, como tipos de pagamento, valores, status e métodos utilizados.
    
    Utilize apenas os dados fornecidos abaixo para construir suas respostas. Seja objetivo, claro e evite suposições. Se os dados não contiverem informações suficientes para responder à pergunta, informe claramente que não há registros correspondentes.
    
    Dados financeiros:
    %s
    """;

    private String buildContextHistory(List<String> contextMessages) {
        if (contextMessages == null || contextMessages.isEmpty()) return "";
        return contextMessages.stream()
                .map(msg -> "History:\n" + msg)
                .collect(Collectors.joining("\n"));
    }

    public String buildPrompt(ChatbotRequest request) {
        String context = String.join("\n", chunkReadService.getRelevantChunks(request.userInput()));
        String history = buildContextHistory(request.context());
        String system = SYSTEM_INSTRUCTIONS.formatted(context);
        return String.join("\n", system, history, "User: " + request.userInput());
    }
}
