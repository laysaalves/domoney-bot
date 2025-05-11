package dev.layseiras.domoney.service;

import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
public class ChunkScheduler {

    private final ChunkService chunkService;

    public ChunkScheduler(ChunkService chunkService) {
        this.chunkService = chunkService;
    }

    @PostConstruct
    public void runOnStartup() {
        System.out.println("Save chunks");
        chunkService.save();
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void updateChunks() {
        System.out.println("Updating chunks!");
        chunkService.save();
    }
}

