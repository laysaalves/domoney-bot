package dev.layseiras.domoney.service;

import dev.layseiras.domoney.documents.Chunk;
import dev.layseiras.domoney.dto.DashboardApiDTO;
import dev.layseiras.domoney.repository.ChunkRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChunkService {

    private final ChunkRepository repository;

    private final DashboardApiService fetchService;
    public ChunkService(ChunkRepository repository, DashboardApiService fetchService) {
        this.repository = repository;
        this.fetchService = fetchService;
    }

    public String generateFormattedChunkId() {
        String letters = RandomStringUtils.randomAlphabetic(3).toUpperCase();
        String numbers = RandomStringUtils.randomNumeric(3);
        return letters + numbers;
    }

    public void save() {
        List<DashboardApiDTO> activities = fetchService.getAllActivities();
        for (DashboardApiDTO activity : activities) {

            boolean exists = repository.existsByOriginId(activity.id());
            if (exists) continue;

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
    private List<String> generateChunks(DashboardApiDTO dto) {
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
