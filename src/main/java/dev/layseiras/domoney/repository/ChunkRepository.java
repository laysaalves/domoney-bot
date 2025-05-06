package dev.layseiras.domoney.repository;

import dev.layseiras.domoney.documents.Chunk;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChunkRepository extends MongoRepository<Chunk, Long> {}
