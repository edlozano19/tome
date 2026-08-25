package com.tome.ingest.repository;

import com.tome.ingest.model.ProcessedEventEntity;
import com.tome.ingest.model.ProcessedEventId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository
    extends JpaRepository<ProcessedEventEntity, ProcessedEventId> {}
