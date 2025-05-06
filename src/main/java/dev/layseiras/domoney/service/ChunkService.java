package dev.layseiras.domoney.service;

import dev.layseiras.domoney.documents.Chunk;
import dev.layseiras.domoney.dto.ExternalApiDTO;
import dev.layseiras.domoney.repository.ChunkRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChunkService {

    private final ChunkRepository repository;

    private final ExternalApiService fetchService;
    public ChunkService(ChunkRepository repository, ExternalApiService fetchService) {
        this.repository = repository;
        this.fetchService = fetchService;
    }

    public String generateFormattedChunkId() {
        String letters = RandomStringUtils.randomAlphabetic(3).toUpperCase();
        String numbers = RandomStringUtils.randomNumeric(3);
        return letters + numbers;
    }

    public void save() {
        List<ExternalApiDTO> activities = fetchService.fetch();
        for (ExternalApiDTO activity : activities) {
            List<String> chunks = generateChunks(activity);
            int index = 0;
            for (String text : chunks) {
                Chunk chunk = new Chunk();
                chunk.setId("chunk_" + generateFormattedChunkId());
                chunk.setOriginId(activity.id());
                chunk.setActivityType(activity.paymentType().toString());
                chunk.setChunkId(index++);
                chunk.setText(text);
                chunk.setUpdatedAt(LocalDateTime.now());
                // generateEmbedding
                repository.save(chunk);
            }
        }
    }
    private List<String> generateChunks(ExternalApiDTO dto) {
        String text = String.format(
                "O item de %s com o nome de %s custou %s reais e está %s via %s.",
                dto.paymentType().toString(),
                dto.title(),
                dto.price(),
                dto.status(),
                dto.method()
        );

        return List.of(text);
    }
}
