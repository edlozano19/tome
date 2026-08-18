package com.tome.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Entity
@Getter
@Table(name = "book_file")
public class BookFileEntity {

  @Id private UUID id;

  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "book_id", nullable = false, unique = true)
  private BookEntity book;

  @Column(nullable = false, length = 64)
  private String sha256;

  @Column(name = "original_filename", length = 500)
  private String originalFilename;

  @Column(name = "content_type", nullable = false, length = 100)
  private String contentType;

  @Column(name = "size_bytes", nullable = false)
  private Long sizeBytes;

  @Column(name = "storage_path", nullable = false, length = 1000)
  private String storagePath;

  @Enumerated(EnumType.STRING)
  @Column(name = "ingest_status", nullable = false, length = 32)
  private IngestStatus ingestStatus;

  @Column(name = "ingest_error")
  private String ingestError;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  protected BookFileEntity() {}

  public BookFileEntity(
      UUID id,
      BookEntity book,
      String sha256,
      String originalFilename,
      Long sizeBytes,
      String storagePath) {
    this.id = id;
    this.book = book;
    this.sha256 = sha256;
    this.originalFilename = originalFilename;
    this.sizeBytes = sizeBytes;
    this.storagePath = storagePath;
    this.ingestError = null;
    this.contentType = "application/epub+zip";
    this.ingestStatus = IngestStatus.PENDING;
    this.createdAt = Instant.now();
  }
}
