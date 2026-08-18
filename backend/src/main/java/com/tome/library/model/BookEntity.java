package com.tome.library.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Entity
@Getter
@Table(name = "book")
public class BookEntity {

  @Id private UUID id;

  @Column(nullable = false, length = 500)
  private String title;

  @Column(nullable = false, length = 300)
  private String author;

  @Column(nullable = false, unique = true, length = 200)
  private String slug;

  @Column private String description;

  @Column(nullable = false, length = 16)
  private String language;

  @Column(name = "is_public_domain", nullable = false)
  private boolean publicDomain;

  @Column(length = 100)
  private String source;

  @Column(name = "cover_url", length = 1000)
  private String coverUrl;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  protected BookEntity() {}

  public BookEntity(
      UUID id,
      String title,
      String author,
      String slug,
      String description,
      String source,
      String coverUrl) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.slug = slug;
    this.description = description;
    this.source = source;
    this.coverUrl = coverUrl;
    this.language = "en";
    this.publicDomain = true;
    Instant now = Instant.now();
    this.createdAt = now;
    this.updatedAt = now;
  }
}
