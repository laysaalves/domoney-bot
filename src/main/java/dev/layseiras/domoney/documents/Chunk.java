package dev.layseiras.domoney.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Document(collection = "chunks")
public class Chunk {
    @Id
    private String id;

    private UUID originId;
    private String activityType;
    private int chunkId;
    private String text;

    private List<Double> embedding;
    private LocalDateTime updatedAt;

    public Chunk() {
    }

    public Chunk(String id, UUID originId, String activityType, int chunkId, String text, List<Double> embedding, LocalDateTime updatedAt) {
        this.id = id;
        this.originId = originId;
        this.activityType = activityType;
        this.chunkId = chunkId;
        this.text = text;
        this.embedding = embedding;
        this.updatedAt = updatedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UUID getOriginId() {
        return originId;
    }

    public void setOriginId(UUID originId) {
        this.originId = originId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public int getChunkId() {
        return chunkId;
    }

    public void setChunkId(int chunkId) {
        this.chunkId = chunkId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Double> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Double> embedding) {
        this.embedding = embedding;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
