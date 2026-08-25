package com.tome.ingest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Entity
@Getter
@Table(
    name = "text_chunk",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uq_text_chunk_chapter_ordinal",
            columnNames = {"chapter_id", "ordinal"}))
public class TextChunkEntity {

  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "chapter_id", nullable = false)
  private ChapterEntity chapter;

  @Column(nullable = false)
  private int ordinal;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String body;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  protected TextChunkEntity() {}
  ; // NOSONAR - required by JPA

  public TextChunkEntity(UUID id, ChapterEntity chapter, int ordinal, String body) {
    this.id = id;
    this.chapter = chapter;
    this.ordinal = ordinal;
    this.body = body;
    this.createdAt = Instant.now();
  }
}
