package com.tome.ingest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Entity
@Getter
@IdClass(ProcessedEventId.class)
@Table(name = "processed_event")
public class ProcessedEventEntity {

  @Id
  @Column(name = "event_id", nullable = false)
  private UUID eventId;

  @Id
  @Column(name = "consumer_name", nullable = false, length = 100)
  private String consumerName;

  @Column(name = "processed_at", nullable = false)
  private Instant processedAt;

  protected ProcessedEventEntity() {} // NOSONAR - required by JPA

  public ProcessedEventEntity(UUID eventId, String consumerName) {
    this.eventId = eventId;
    this.consumerName = consumerName;
    this.processedAt = Instant.now();
  }
}
