package com.tome.ingest.model;

import com.tome.library.model.BookEntity;
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
    name = "chapter",
    uniqueConstraints =
        @UniqueConstraint(
            name = "uq_chapter_book_ordinal",
            columnNames = {"book_id", "ordinal"}))
public class ChapterEntity {

  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "book_id", nullable = false)
  private BookEntity book;

  @Column(nullable = false)
  private int ordinal;

  @Column(length = 500)
  private String title;

  @Column(name = "spine_href", length = 1000)
  private String spineHref;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  protected ChapterEntity() {} // NOSONAR - required by JPA

  public ChapterEntity(UUID id, BookEntity book, int ordinal, String title, String spineHref) {
    this.id = id;
    this.book = book;
    this.ordinal = ordinal;
    this.title = title;
    this.spineHref = spineHref;
    this.createdAt = Instant.now();
  }
}
