package com.tome.ingest.repository;

import com.tome.ingest.model.TextChunkEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextChunkRepository extends JpaRepository<TextChunkEntity, UUID> {}
