package dev.layseiras.domoney.service;

import dev.layseiras.domoney.documents.Chunk;
import dev.layseiras.domoney.repository.ChunkRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChunkReadService {

    private final ChunkRepository repository;

    public ChunkReadService(ChunkRepository repository) {
        this.repository = repository;
    }

    public List<String> getRelevantChunks(String userInput) {
        String[] keywords = userInput.toLowerCase().split("\\s+");

        return repository.findAll()
                .stream()
                .filter(chunk -> {
                    String text = chunk.getText().toLowerCase();
                    for (String keyword : keywords) {
                        if (text.contains(keyword)) return true;
                    }
                    return false;
                })
                .map(Chunk::getText)
                .limit(3)
                .collect(Collectors.toList());
    }
}

